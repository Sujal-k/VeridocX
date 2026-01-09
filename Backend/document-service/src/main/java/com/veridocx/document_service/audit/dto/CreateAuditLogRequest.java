package com.veridocx.document_service.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuditLogRequest {
    private UUID userId;
    private String action;
    private String details;
}
