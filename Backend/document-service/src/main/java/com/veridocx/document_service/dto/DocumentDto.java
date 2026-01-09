package com.veridocx.document_service.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class DocumentDto {

    private UUID id;
    private UUID userId;
    private String filename;
    private String path;
}