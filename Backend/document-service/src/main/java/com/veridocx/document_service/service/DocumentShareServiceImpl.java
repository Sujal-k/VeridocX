package com.veridocx.document_service.service;

import com.veridocx.document_service.entity.DocumentShare;
import com.veridocx.document_service.repo.DocumentShareRepository;
import com.veridocx.document_service.security.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentShareServiceImpl implements DocumentShareService {

    private final DocumentShareRepository repo;

    @Override
    public void shareDocument(String documentId, String ownerId, String targetUserId) {

        var share = DocumentShare.builder()
                .documentId(documentId)
                .ownerId(ownerId)
                .sharedWith(targetUserId)
                .permission(Permission.valueOf("READ"))
                .build();

        repo.save(share);
    }


    @Override
    public boolean hasAccess(String documentId, String userId) {
        return repo.findByDocumentIdAndSharedWith(documentId, userId).isPresent();
    }
}
