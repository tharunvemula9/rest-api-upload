package com.finra.repositories.files;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FILE_UPLOAD")
public class FileUpload {
	
	private Long Id;
	private String fileName;
	private byte[] data;
	
	@Id
    @GeneratedValue
    @Column(name = "FILE_ID")
	public Long getId() {
		return Id;
	}
	
	
	public void setId(Long id) {
		Id = id;
	}
	
	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name = "FILE_DATA")
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
	

}
