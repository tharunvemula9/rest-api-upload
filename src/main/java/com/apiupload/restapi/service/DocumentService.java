package com.apiupload.restapi.service;

import java.util.Date;
import java.util.List;

import com.apiupload.restapi.docs.Document;
import com.apiupload.restapi.docs.DocumentMetadata;


public interface DocumentService {
    
    DocumentMetadata save(Document document);
    
    List<DocumentMetadata> findDocuments(String personName, Date date);
    
    byte[] getDocumentFile(String id);

	List<DocumentMetadata> findDocumentsByCriteria(String person, Date before, Date after);
}
