package com.veridocx.document_service.repo;

import com.veridocx.document_service.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, String> {

    List<Document> findByUserId(String userId);

    Optional<Document> findTopByUserIdAndFilenameOrderByVersionDesc(
            String userId,
            String filename
    );


    Optional<Document> findByUserIdAndFilenameAndVersion(
            String userId,
            String filename,
            int version
    );
    @Query("""
SELECT d FROM Document d
WHERE d.userId = :userId
AND d.version = (
    SELECT MAX(d2.version)
    FROM Document d2
    WHERE d2.userId = :userId
    AND d2.filename = d.filename
)
ORDER BY d.uploadedAt DESC
""")
    List<Document> findLatestDocumentsByUser(String userId);

}

