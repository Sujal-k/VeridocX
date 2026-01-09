package com.veridocx.audit_service.controller;

import com.veridocx.audit_service.dto.CreateAuditLogRequest;
import com.veridocx.audit_service.entity.AuditLog;
import com.veridocx.audit_service.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit")
public class AuditLogController {

    private final AuditLogService service;

    public AuditLogController(AuditLogService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AuditLog> create(@RequestBody CreateAuditLogRequest req) {
        return ResponseEntity.ok(
                service.log(req.getUserId(), req.getAction(), req.getDetails())
        );
    }

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLog>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.getByUser(userId));
    }

    @GetMapping("/action/{action}")
    public ResponseEntity<List<AuditLog>> getByAction(@PathVariable String action) {
        return ResponseEntity.ok(service.getByAction(action));
    }
}
