package com.apiupload.restapi.dao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.apiupload.restapi.docs.Document;
import com.apiupload.restapi.docs.DocumentMetadata;

public interface DocumentDao {

	//get file from webclient and save
    void insert(Document document);
    
    //find document list with some metadata like person name and creation date
    List<DocumentMetadata> findByPersonNameDate(String personName, Date date);
    
    //find document list with some metadata like person name and creation before and after dates
    List<DocumentMetadata> findByPersonNameBeforedateAfterdate(String personName, Date before, Date after);
    
    //load document
    Document load(String uuid);

	List<DocumentMetadata> getRecentList(Date date) throws IOException;
    
}
