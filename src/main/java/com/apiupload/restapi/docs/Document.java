package com.apiupload.restapi.docs;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

import com.apiupload.restapi.service.DocumentService;

/**
 * A document from an store managed by {@link DocumentService}.
 * 
 */
public class Document extends DocumentMetadata implements Serializable {

    private static final long serialVersionUID = 2004955454853853315L;
    
    private byte[] fileData;
    
    public Document( byte[] fileData, String fileName, Date documentDate, String personName) {
        super(fileName, documentDate, personName);
        this.fileData = fileData;
    }

    public Document(Properties properties) {
        super(properties);
    }
    
    public Document(DocumentMetadata metadata) {
        super(metadata.getUuid(), metadata.getFileName(), metadata.getDocumentDate(), metadata.getPersonName());
    }

    public byte[] getFileData() {
        return fileData;
    }
    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
    
    public DocumentMetadata getMetadata() {
        return new DocumentMetadata(getUuid(), getFileName(), getDocumentDate(), getPersonName());
    }
    
}
