package com.veridocx.verification_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "verifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Verification {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID documentId;
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * Human-readable explanation of the verification result.
     * Used for audit, UI, and compliance reporting.
     */
    private String reason;

    /**
     * SHA-256 checksum of the verified file.
     */
    private String checksum;

    private Instant createdAt;
    private Instant completedAt;

    /**
     * Enterprise-grade verification verdicts.
     */
    public enum Status {
        PENDING,     // verification started
        VALID,       // hash + metadata match
        SUSPICIOUS,  // hash ok, but metadata / structure anomaly
        TAMPERED     // hash mismatch or major alteration detected
    }
}
