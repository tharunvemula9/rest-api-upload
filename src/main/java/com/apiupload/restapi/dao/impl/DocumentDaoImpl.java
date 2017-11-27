package com.apiupload.restapi.dao.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;

import com.apiupload.restapi.dao.DocumentDao;
import com.apiupload.restapi.docs.Document;
import com.apiupload.restapi.docs.DocumentMetadata;

import org.springframework.stereotype.Service;

@Service("documentDao")
public class DocumentDaoImpl implements DocumentDao {

    private static final Logger LOG = Logger.getLogger(DocumentDaoImpl.class);
    
    public static final String DIRECTORY = "datastore";
    public static final String META_DATA_FILE_NAME = "metadata.properties";
    
    @PostConstruct
    public void init() {
        createDirectory(DIRECTORY);
    }
    
    @Override
    public void insert(Document document) {
        try {
            createDirectory(document);
            saveFileData(document);
            saveMetaData(document);
        } catch (IOException e) {
            String message = "Error while inserting document";
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
    
    @Override
    public List<DocumentMetadata> findByPersonNameDate(String personName, Date date) {
        try {
            return findInFileSystem(personName,date);
        } catch (IOException e) {
            String message = "Error while finding document, person name: " + personName + ", date:" + date;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
    
	@Override
	public List<DocumentMetadata> findByPersonNameBeforedateAfterdate(String personName, Date before, Date after) {
        try {
            return findInFileSystem(personName,before, after);
        } catch (IOException e) {
            String message = "Error while finding document, person name: " + personName + ", before date:" + before + " after date: " + after;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
	}
    
    private List<DocumentMetadata> findInFileSystem(String personName, Date before, Date after) throws IOException{
    	//TODO check before greater than after date
    		//if true
		    	List<String> uuidList = getUuidList();
		        List<DocumentMetadata> metadataList = new ArrayList<DocumentMetadata>(uuidList.size());
		        for (String uuid : uuidList) {
		            DocumentMetadata metadata = loadMetadataFromFileSystem(uuid);         
		            if(isBeforeDateAndAfteDate(metadata, personName, before, after)) {
		                metadataList.add(metadata);
		            }
		        }
		        return metadataList;
		    //else
		        // find in files before date, or find in files after date
		        //isAfterDateOrBeforeDDate
	}

	private boolean isBeforeDateAndAfteDate(DocumentMetadata metadata, String personName, Date beforeDate, Date afterDate) {
		if(metadata==null) {
            return false;
        }
        boolean match = true;
        if(personName!=null) {
            match = (personName.equals(metadata.getPersonName()));
        }
        if(match && afterDate!=null && beforeDate!=null) {
        	Date date = metadata.getDocumentDate();
            match = date.after(afterDate) && date.before(beforeDate);
        }
        return match;
	}

	@Override
    public Document load(String uuid) {
        try {
            return loadFromFileSystem(uuid);
        } catch (IOException e) {
            String message = "Error while loading document with id: " + uuid;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
        
    }
    

    private List<DocumentMetadata> findInFileSystem(String personName, Date date) throws IOException  {
        List<String> uuidList = getUuidList();
        List<DocumentMetadata> metadataList = new ArrayList<DocumentMetadata>(uuidList.size());
        for (String uuid : uuidList) {
            DocumentMetadata metadata = loadMetadataFromFileSystem(uuid);         
            if(isMatched(metadata, personName, date)) {
                metadataList.add(metadata);
            }
        }
        return metadataList;
    }
    

    private boolean isMatched(DocumentMetadata metadata, String personName, Date date) {
        if(metadata==null) {
            return false;
        }
        boolean match = true;
        if(personName!=null) {
            match = (personName.equals(metadata.getPersonName()));
        }
        if(match && date!=null) {
            match = (date.equals(metadata.getDocumentDate()));
        }
        return match;
    }

    private DocumentMetadata loadMetadataFromFileSystem(String uuid) throws IOException {
        DocumentMetadata document = null;
        String dirPath = getDirectoryPath(uuid);
        File file = new File(dirPath);
        if(file.exists()) {
            Properties properties = readProperties(uuid);
            document = new DocumentMetadata(properties);
            
        } 
        return document;
    }
    
    private Document loadFromFileSystem(String uuid) throws IOException {
       DocumentMetadata metadata = loadMetadataFromFileSystem(uuid);
       if(metadata==null) {
           return null;
       }
       Path path = Paths.get(getFilePath(metadata));
       Document document = new Document(metadata);
       document.setFileData(Files.readAllBytes(path));
       return document;
    }

    private String getFilePath(DocumentMetadata metadata) {
        String dirPath = getDirectoryPath(metadata.getUuid());
        StringBuilder sb = new StringBuilder();
        sb.append(dirPath).append(File.separator).append(metadata.getFileName());
        return sb.toString();
    }
    
    private void saveFileData(Document document) throws IOException {
        String path = getDirectoryPath(document);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(new File(path), document.getFileName())));
        stream.write(document.getFileData());
        stream.close();
    }
    
    public void saveMetaData(Document document) throws IOException {
            String path = getDirectoryPath(document);
            Properties props = document.createProperties();
            File f = new File(new File(path), META_DATA_FILE_NAME);
            OutputStream out = new FileOutputStream( f );
            props.store(out, "Document meta data");       
    }
    
    private List<String> getUuidList() {
        File file = new File(DIRECTORY);
        String[] directories = file.list(new FilenameFilter() {
          @Override
          public boolean accept(File current, String name) {
            return new File(current, name).isDirectory();
          }
        });
        return Arrays.asList(directories);
    }
    
    private Properties readProperties(String uuid) throws IOException {
        Properties prop = new Properties();
        InputStream input = null;     
        try {
            input = new FileInputStream(new File(getDirectoryPath(uuid),META_DATA_FILE_NAME));
            prop.load(input);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }
    
    private String createDirectory(Document document) {
        String path = getDirectoryPath(document);
        createDirectory(path);
        return path;
    }

    private String getDirectoryPath(Document document) {
       return getDirectoryPath(document.getUuid());
    }
    
    private String getDirectoryPath(String uuid) {
        StringBuilder sb = new StringBuilder();
        sb.append(DIRECTORY).append(File.separator).append(uuid);
        String path = sb.toString();
        return path;
    }

    private void createDirectory(String path) {
        File file = new File(path);
        file.mkdirs();
    }
    
    public List<DocumentMetadata> getRecentList(Date curr) throws IOException{
    	
    	List<String> uuidList = getUuidList();
    	List<DocumentMetadata> metadataList = new ArrayList<DocumentMetadata>(uuidList.size());
        for (String uuid : uuidList) {
            DocumentMetadata metadata;
				metadata = loadMetadataFromFileSystem(uuid);
				long timeDifferenceMilliseconds = curr.getTime() - metadata.getDocumentDate().getTime();
	            double diffDays = timeDifferenceMilliseconds / (60 * 60 * 1000 * 24);
	            if(timeDifferenceMilliseconds < 1.000 ) {
	                metadataList.add(metadata);
	            }
        }
        return metadataList;
    }

}
