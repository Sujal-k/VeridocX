package com.veridocx.document_service.audit.client;

import com.veridocx.document_service.audit.AuditAction;
import com.veridocx.document_service.audit.dto.CreateAuditLogRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
@Slf4j
public class AuditClient {

    private final RestTemplate restTemplate;

    @Value("${audit.service.url}")
    private String auditServiceUrl;

    public AuditClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void log(UUID userId, AuditAction action, String details) {
        try {
            CreateAuditLogRequest req =
                    new CreateAuditLogRequest(userId, action.name(), details);

            restTemplate.postForEntity(
                    auditServiceUrl + "/api/v1/audit",
                    req,
                    Void.class
            );
        } catch (Exception ex) {
            log.error("Audit logging failed: {}", ex.getMessage());
        }
    }
}
