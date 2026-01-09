package com.veridocx.audit_service.service;

import com.veridocx.audit_service.entity.AuditLog;

import java.util.List;
import java.util.UUID;

public interface AuditLogService {
    AuditLog log(UUID userId, String action, String details);
    List<AuditLog> getByUser(UUID userId);
    List<AuditLog> getByAction(String action);
    List<AuditLog> getAll();
}
