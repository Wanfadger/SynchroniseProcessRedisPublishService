package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table
//@Cache(region = "StaffFaceId", usage = CacheConcurrencyStrategy.READ_WRITE)
// thumbnail
public class StaffFaceId extends ParentEntity{
	
	private String fileName;
	private String fileSize;   
	private String folder;
	private String refId; 
	private String filePath; 
	
	public StaffFaceId() {
		
	}

	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	

}
