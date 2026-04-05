package com.veridocx.verification_service.client;


import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
public class AuditClient {

    private final WebClient webClient;

    public AuditClient(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public void log(UUID userId, String action, String details) {
        try {
            webClient.post()
                    .uri("https://veridocx-apigateway.onrender.com/api/v1/audit")
                    .bodyValue(new AuditRequest(userId, action, details))
                    .retrieve()
                    .toBodilessEntity()
                    .subscribe(); // 🔥 non-blocking
        } catch (Exception ignored) {
            // audit must never break verification
        }
    }

    record AuditRequest(UUID userId, String action, String details) {}
}
