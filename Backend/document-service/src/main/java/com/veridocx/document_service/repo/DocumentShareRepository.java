package com.veridocx.document_service.repo;

import com.veridocx.document_service.entity.DocumentShare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.*;

public interface DocumentShareRepository
        extends JpaRepository<DocumentShare, Long> {

    List<DocumentShare> findBySharedWith(String sharedWith);

    Optional<DocumentShare> findByDocumentIdAndSharedWith(
            String documentId,
            String sharedWith
    );
}
