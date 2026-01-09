package com.veridocx.document_service.service;

import com.veridocx.document_service.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    Document upload(MultipartFile file, String userId) throws Exception;

    Document getById(String id);

    List<Document> getByUser(String userId);

    List<Document> getLatestByUser(String userId);

    void delete(String id) throws Exception;

    List<Document> getSharedWithUser(String userId);


}
