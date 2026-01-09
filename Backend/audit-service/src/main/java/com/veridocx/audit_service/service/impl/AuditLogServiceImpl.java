package com.veridocx.audit_service.service.impl;

import com.veridocx.audit_service.entity.AuditLog;
import com.veridocx.audit_service.repo.AuditLogRepository;
import com.veridocx.audit_service.service.AuditLogService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository repo;

    public AuditLogServiceImpl(AuditLogRepository repo) {
        this.repo = repo;
    }

    @Override
    public AuditLog log(UUID userId, String action, String details) {
        AuditLog log = AuditLog.builder()
                .userId(userId)
                .action(action)
                .details(details)
                .timestamp(Instant.now())
                .build();
        return repo.save(log);
    }

    @Override
    public List<AuditLog> getByUser(UUID userId) {
        return repo.findByUserId(userId);
    }

    @Override
    public List<AuditLog> getByAction(String action) {
        return repo.findByAction(action);
    }

    @Override
    public List<AuditLog> getAll() {
        return repo.findAll();
    }
}
