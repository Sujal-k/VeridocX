package com.veridocx.audit_service.repo;

import com.veridocx.audit_service.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByUserId(UUID userId);
    List<AuditLog> findByAction(String action);
}
