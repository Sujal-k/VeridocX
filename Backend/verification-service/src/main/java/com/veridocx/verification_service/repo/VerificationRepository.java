package com.veridocx.verification_service.repo;

import com.veridocx.verification_service.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VerificationRepository extends JpaRepository<Verification, UUID> {
    List<Verification> findByUserId(UUID userId);
    List<Verification> findByDocumentId(UUID documentId);
}
