package com.finra.dao;

import com.finra.repositories.files.FileUpload;

public interface FileUploadDAO {
	public void save(FileUpload fileUpload);
	public void delete();
}
