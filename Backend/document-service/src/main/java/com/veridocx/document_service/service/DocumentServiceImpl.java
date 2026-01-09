package com.veridocx.document_service.service;

import com.veridocx.document_service.audit.AuditAction;
import com.veridocx.document_service.audit.client.AuditClient;
import com.veridocx.document_service.entity.Document;
import com.veridocx.document_service.repo.DocumentRepository;
import com.veridocx.document_service.repo.DocumentShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository repo;
    private final AuditClient auditClient;
    private final DocumentShareRepository shareRepo;

    @Value("${document.upload-dir}")
    private String uploadDir;

    @Override
    public Document upload(MultipartFile file, String userId) throws Exception {

        Files.createDirectories(Paths.get(uploadDir));

        String filename = file.getOriginalFilename();

        int nextVersion = repo
                .findTopByUserIdAndFilenameOrderByVersionDesc(userId, filename)
                .map(d -> d.getVersion() + 1)
                .orElse(1);

        String docId = UUID.randomUUID().toString();
        String storedPath =
                uploadDir + "/" + userId + "_" + filename + "_v" + nextVersion;

        Files.write(Paths.get(storedPath), file.getBytes());

        Document doc = Document.builder()
                .id(docId)
                .userId(userId)
                .filename(filename)
                .path(storedPath)
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .version(nextVersion)
                .build();

        Document saved = repo.save(doc);

        // 🔐 AUDIT LOG (non-blocking)
        auditClient.log(
                UUID.fromString(userId),
                AuditAction.DOCUMENT_UPLOADED,
                "Uploaded document " + filename + " v" + nextVersion
        );

        return saved;
    }

    @Override
    public Document getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    @Override
    public List<Document> getByUser(String userId) {
        return repo.findByUserId(userId);
    }

    @Override
    public List<Document> getLatestByUser(String userId) {
        return repo.findLatestDocumentsByUser(userId);
    }
    @Override
    public void delete(String id) throws Exception {
        Document doc = getById(id);

        Files.deleteIfExists(Paths.get(doc.getPath()));

        repo.delete(doc);
    }
    @Override
    public List<Document> getSharedWithUser(String userId) {

        var shares = shareRepo.findBySharedWith(userId);

        return shares.stream()
                .map(s -> getById(s.getDocumentId()))
                .toList();
    }

}
