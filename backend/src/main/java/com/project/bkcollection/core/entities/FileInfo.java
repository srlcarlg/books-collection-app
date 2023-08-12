package com.project.bkcollection.core.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FileInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String filename;
    private Long contentLength;
    private String contentType;
    private String url;
    
    public FileInfo() {
		// TODO Auto-generated constructor stub
	}   
	public FileInfo(Long id, String filename, Long contentLength, String contentType, String url) {
		super();
		this.id = id;
		this.filename = filename;
		this.contentLength = contentLength;
		this.contentType = contentType;
		this.url = url;
	}

	public Long getId() {
		return id;
	}
	public String getFilename() {
		return filename;
	}
	public Long getContentLength() {
		return contentLength;
	}
	public String getContentType() {
		return contentType;
	}
	public String getUrl() {
		return url;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileInfo other = (FileInfo) obj;
		return Objects.equals(id, other.id);
	}
	
	@Override
	public String toString() {
		return "FileInfo [id=" + id + ", filename=" + filename + ", contentLength=" + contentLength + ", contentType="
				+ contentType + ", url=" + url + "]";
	}     
	
}
