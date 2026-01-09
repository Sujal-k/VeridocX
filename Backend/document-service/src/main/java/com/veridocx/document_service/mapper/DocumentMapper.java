package com.veridocx.document_service.mapper;

import com.veridocx.document_service.dto.DocumentDto;
import com.veridocx.document_service.entity.Document;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DocumentMapper {

    public DocumentDto toDto(Document d) {
        DocumentDto dto = new DocumentDto();
        dto.setId(UUID.fromString(d.getId()));
        dto.setUserId(UUID.fromString(d.getUserId()));
        dto.setFilename(d.getFilename());
        dto.setPath(d.getPath());
        return dto;
     }
}