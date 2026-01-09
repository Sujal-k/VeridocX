package com.veridocx.document_service.entity;

import com.veridocx.document_service.security.Permission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "document_shares",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"document_id", "shared_with"}
        )
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentShare {

    @Id
    @GeneratedValue
    private UUID id;

    private String documentId;
    private String ownerId;
    private String sharedWith;

    @Enumerated(EnumType.STRING)
    private Permission permission;

    private LocalDateTime sharedAt;
}

