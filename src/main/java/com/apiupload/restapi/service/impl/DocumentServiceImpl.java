package com.apiupload.restapi.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.apiupload.restapi.dao.DocumentDao;
import com.apiupload.restapi.docs.Document;
import com.apiupload.restapi.docs.DocumentMetadata;
import com.apiupload.restapi.service.DocumentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("documentService")
public class DocumentServiceImpl implements DocumentService, Serializable {

    private static final long serialVersionUID = 8119784722798361327L;
    
    @Autowired
    private DocumentDao DocumentDao;

    @Override
    public DocumentMetadata save(Document document) {
        getDocumentDao().insert(document); 
        return document.getMetadata();
    }
    
    @Override
    public List<DocumentMetadata> findDocuments(String personName, Date date) {
        return getDocumentDao().findByPersonNameDate(personName, date);
    }
    
    @Override
    public byte[] getDocumentFile(String id) {
        Document document = getDocumentDao().load(id);
        if(document!=null) {
            return document.getFileData();
        } else {
            return null;
        }
    }


    public DocumentDao getDocumentDao() {
        return DocumentDao;
    }

    public void setDocumentDao(DocumentDao documentDao) {
        DocumentDao = documentDao;
    }

	@Override
	public List<DocumentMetadata> findDocumentsByCriteria(String person, Date before, Date after) {
		return getDocumentDao().findByPersonNameBeforedateAfterdate(person, before, after);
	}


}
