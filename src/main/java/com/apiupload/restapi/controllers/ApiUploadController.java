package com.apiupload.restapi.controllers;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.apiupload.restapi.docs.Document;
import com.apiupload.restapi.docs.DocumentMetadata;
import com.apiupload.restapi.service.DocumentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping(value = "/apiupload")
public class ApiUploadController {

    private static final Logger LOG = Logger.getLogger(ApiUploadController.class);
    
    @Autowired
    DocumentService documentService;


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody DocumentMetadata handleFileUpload(
            @RequestParam(value="file", required=true) MultipartFile file ,
            @RequestParam(value="person", required=true) String person,
            @RequestParam(value="date", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
        
        try {
            Document document = new Document(file.getBytes(), file.getOriginalFilename(), date, person );
            getDocumentService().save(document);
            return document.getMetadata();
        } catch (RuntimeException e) {
            LOG.error("Error while uploading.", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Error while uploading.", e);
            throw new RuntimeException(e);
        }      
    }
    
   
    @RequestMapping(value = "/documents", method = RequestMethod.GET)
    public HttpEntity<List<DocumentMetadata>> findDocument(
            @RequestParam(value="person", required=false) String person,
            @RequestParam(value="date", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<List<DocumentMetadata>>(getDocumentService().findDocuments(person,date), httpHeaders,HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public HttpEntity<List<DocumentMetadata>> findDocumentsByCriteria(
            @RequestParam(value="person", required=false) String person,
            @RequestParam(value="before", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date before,
            @RequestParam(value="after", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date after) {
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<List<DocumentMetadata>>(getDocumentService().findDocumentsByCriteria(person,before, after), httpHeaders,HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "/document/{id}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getDocument(@PathVariable String id) {         
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<byte[]>(getDocumentService().getDocumentFile(id), httpHeaders, HttpStatus.OK);
    }

    public DocumentService getDocumentService() {
        return documentService;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

}
