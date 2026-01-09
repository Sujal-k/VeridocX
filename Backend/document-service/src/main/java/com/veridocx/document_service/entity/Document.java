package com.veridocx.document_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    private String id;

    private String userId;
    private String filename;
    private String path;
    private String contentType;
    private Long fileSize;
    private Integer version;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    @PrePersist

    public void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }
}
