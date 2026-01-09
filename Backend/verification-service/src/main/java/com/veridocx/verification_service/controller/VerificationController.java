    package com.veridocx.verification_service.controller;
    
    import com.veridocx.verification_service.dto.CreateVerificationRequest;
    import com.veridocx.verification_service.dto.VerificationResponse;
    import com.veridocx.verification_service.entity.Verification;
    import com.veridocx.verification_service.service.VerificationService;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    
    import java.util.UUID;
    import java.util.stream.Collectors;
    
    @RestController
    @RequestMapping("/api/v1/verify")
    public class VerificationController {
    
        private final VerificationService svc;
    
        public VerificationController(VerificationService svc) {
            this.svc = svc;
        }
    
        @PostMapping
        public ResponseEntity<VerificationResponse> verify(@RequestBody CreateVerificationRequest req) throws Exception {
            Verification v = svc.createAndRunVerification(req.getDocumentId(), req.getUserId(), req.getFileUrl());
            return ResponseEntity.ok(toResponse(v));
        }
    
        @GetMapping("/{id}")
        public ResponseEntity<VerificationResponse> get(@PathVariable UUID id) {
            return ResponseEntity.ok(toResponse(svc.getById(id)));
        }
    
        @GetMapping("/document/{documentId}")
        public ResponseEntity<?> byDocument(@PathVariable UUID documentId) {
            return ResponseEntity.ok(svc.getByDocumentId(documentId).stream().map(this::toResponse).collect(Collectors.toList()));
        }
    
        @GetMapping("/user/{userId}")
        public ResponseEntity<?> byUser(@PathVariable UUID userId) {
            return ResponseEntity.ok(svc.getByUserId(userId).stream().map(this::toResponse).collect(Collectors.toList()));
        }
    
        private VerificationResponse toResponse(Verification v) {
            VerificationResponse r = new VerificationResponse();
            r.setId(v.getId());
            r.setDocumentId(v.getDocumentId());
            r.setUserId(v.getUserId());
            r.setStatus(v.getStatus().name());
            r.setReason(v.getReason());
            r.setChecksum(v.getChecksum());
            r.setCreatedAt(v.getCreatedAt());
            r.setCompletedAt(v.getCompletedAt());
            return r;
        }
    }
