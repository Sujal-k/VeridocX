package com.veridocx.verification_service.service.impl;

import com.veridocx.verification_service.client.AuditClient;
import com.veridocx.verification_service.entity.Verification;
import com.veridocx.verification_service.repo.VerificationRepository;
import com.veridocx.verification_service.service.VerificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class VerificationServiceImpl implements VerificationService {

    private final VerificationRepository repo;
    private final WebClient webClient;
    private final AuditClient auditClient;

    @Value("${verification.max-download-size:52428800}")
    private long maxDownloadSize;



    public VerificationServiceImpl(
            VerificationRepository repo,

            WebClient webClient,          // <-- inject configured bean

            AuditClient auditClient
    ) {
        this.repo = repo;
        this.webClient = webClient;
        this.auditClient = auditClient;
    }

    @Override
    public Verification createAndRunVerification(UUID documentId, UUID userId, String fileUrl) throws Exception {

        Verification v = Verification.builder()
                .documentId(documentId)
                .userId(userId)
                .status(Verification.Status.PENDING)
                .createdAt(Instant.now())
                .build();

        repo.save(v);

        byte[] data = fetchFileBytes(documentId, fileUrl);

        if (data == null || data.length == 0) {
            return tampered(v, "File empty or not found");
        }

        if (data.length > maxDownloadSize) {
            return tampered(v, "File exceeds allowed size");
        }

        String checksum = sha256(data);
        v.setChecksum(checksum);

        List<Verification> previous = repo.findByDocumentId(documentId);

        Verification lastValid = previous.stream()
                .filter(p -> p.getStatus() == Verification.Status.VALID)
                .max(Comparator.comparing(Verification::getCompletedAt))
                .orElse(null);

        if (lastValid != null && !lastValid.getChecksum().equals(checksum)) {
            return tampered(v, "Checksum mismatch — document modified");
        }

        // ✅ VALID RESULT
        v.setStatus(Verification.Status.VALID);
        v.setReason(lastValid == null
                ? "Initial integrity verification successful"
                : "Checksum matches previous verified version");
        v.setCompletedAt(Instant.now());

        Verification saved = repo.save(v);

        // 🔔 AUDIT LOG (NON-BLOCKING)
        auditClient.log(
                userId,
                "DOCUMENT_VERIFIED",
                "documentId=" + documentId + ", status=VALID"
        );

        return saved;
    }

    // ================= HELPER METHODS =================

    private byte[] fetchFileBytes(UUID documentId, String fileUrl) throws Exception {

        if (fileUrl != null && !fileUrl.isBlank()) {

            if (fileUrl.startsWith("/") || fileUrl.startsWith("C:")) {
                return Files.readAllBytes(Paths.get(fileUrl));
            }

            return webClient.get()
                    .uri(fileUrl)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
        }

        // ✅ CORRECT DOCUMENT SERVICE PORT
        String downloadUrl =
                "http://document-service:8083/api/v1/documents/internal/download/" + documentId;

        return webClient.get()
                .uri(downloadUrl)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }

    private String sha256(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(data);

        StringBuilder hex = new StringBuilder();
        for (byte b : digest) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    private Verification tampered(Verification v, String reason) {

        v.setStatus(Verification.Status.TAMPERED);
        v.setReason(reason);
        v.setCompletedAt(Instant.now());

        Verification saved = repo.save(v);

        // 🔔 AUDIT LOG FOR TAMPERED
        auditClient.log(
                v.getUserId(),
                "DOCUMENT_VERIFIED",
                "documentId=" + v.getDocumentId()
                        + ", status=TAMPERED, reason=" + reason
        );

        return saved;
    }

    // ================= READ METHODS =================

    @Override
    public Verification getById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Verification not found"));
    }

    @Override
    public List<Verification> getByUserId(UUID userId) {
        return repo.findByUserId(userId);
    }

    @Override
    public List<Verification> getByDocumentId(UUID documentId) {
        return repo.findByDocumentId(documentId);
    }
}