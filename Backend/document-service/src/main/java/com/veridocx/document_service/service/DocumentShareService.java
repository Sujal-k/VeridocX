package com.veridocx.document_service.service;

public interface DocumentShareService {

    void shareDocument(String documentId, String ownerId, String targetUserId);

    boolean hasAccess(String documentId, String userId);
}
