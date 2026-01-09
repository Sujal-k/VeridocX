package com.veridocx.audit_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;

    @Column(nullable = false)
    private String action;

    @Column(columnDefinition = "TEXT")
    private String details;

    private Instant timestamp;
}
