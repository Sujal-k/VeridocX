package com.veridocx.document_service.controller;

import com.veridocx.document_service.audit.AuditAction;
import com.veridocx.document_service.audit.client.AuditClient;
import com.veridocx.document_service.entity.Document;
import com.veridocx.document_service.service.DocumentService;
import com.veridocx.document_service.service.DocumentShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService service;
    private final DocumentShareService shareService;
    private final AuditClient auditClient; // 🔐 AUDIT

    // 1️⃣ Upload document (JWT required)
    @PostMapping("/upload")
    public ResponseEntity<Document> upload(
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        String userId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();

        return ResponseEntity.ok(service.upload(file, userId));
    }

    // 2️⃣ Get document metadata (OWNER ONLY)
    @GetMapping("/{id}")
    public ResponseEntity<Document> getOne(@PathVariable String id) {

        String userId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();

        Document doc = service.getById(id);

        if (!doc.getUserId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(doc);
    }

    // 3️⃣ Get my documents
    @GetMapping("/my")
    public ResponseEntity<List<Document>> getMyDocuments() {

        String userId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();

        return ResponseEntity.ok(service.getByUser(userId));
    }

    // 4️⃣ Get my latest documents (versioned)
    @GetMapping("/my/latest")
    public ResponseEntity<List<Document>> getMyLatestDocuments() {

        String userId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();

        return ResponseEntity.ok(service.getLatestByUser(userId));
    }

    // 5️⃣ Share document (OWNER ONLY)
    @PostMapping("/share/{documentId}")
    public ResponseEntity<Void> shareDocument(
            @PathVariable String documentId,
            @RequestParam String targetUserId
    ) {

        String ownerId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();

        shareService.shareDocument(documentId, ownerId, targetUserId);

        // 🔐 AUDIT LOG — DOCUMENT SHARED
        auditClient.log(
                UUID.fromString(ownerId),
                AuditAction.DOCUMENT_SHARED,
                "Shared document " + documentId + " with user " + targetUserId
        );

        return ResponseEntity.ok().build();
    }


    // 6️⃣ Download document (OWNER or SHARED USER)
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable String id) throws Exception {

        String userId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();

        Document doc = service.getById(id);

        if (!doc.getUserId().equals(userId)
                && !shareService.hasAccess(doc.getId(), userId)) {
            return ResponseEntity.status(403).build();
        }

        Path filePath = Paths.get(doc.getPath());
        byte[] bytes = Files.readAllBytes(filePath);

        // 🔐 AUDIT LOG — DOCUMENT DOWNLOADED
        auditClient.log(
                UUID.fromString(userId),
                AuditAction.DOCUMENT_DOWNLOADED,
                "Downloaded document " + doc.getFilename() + " v" + doc.getVersion()
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + doc.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }
    @GetMapping("/internal/download/{id}")
    public ResponseEntity<byte[]> internalDownload(@PathVariable String id) throws Exception {

        Document doc = service.getById(id);

        Path filePath = Paths.get(doc.getPath());
        byte[] bytes = Files.readAllBytes(filePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + doc.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }
    // 7️⃣ Delete document (OWNER ONLY)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) throws Exception {

        String userId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();

        Document doc = service.getById(id);

        if (!doc.getUserId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        service.delete(id);

        auditClient.log(
                UUID.fromString(userId),
                AuditAction.DOCUMENT_DELETED,
                "Deleted document " + id
        );

        return ResponseEntity.noContent().build();
    }
    // 7️⃣ Get documents shared WITH ME
    @GetMapping("/shared")
    public ResponseEntity<List<Document>> getSharedWithMe() {

        String userId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();

        return ResponseEntity.ok(service.getSharedWithUser(userId));
    }


}
