package com.veridocx.verification_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateVerificationRequest {
    private UUID documentId;    // optional if fileUrl provided
    private UUID userId;
    private String fileUrl;     // optional - use for local testing (example: /mnt/data/Sujal_Kesharwani_Resumae.pdf)
}
