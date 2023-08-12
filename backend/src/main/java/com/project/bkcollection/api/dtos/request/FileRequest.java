package com.project.bkcollection.api.dtos.request;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonNaming(SnakeCaseStrategy.class)
public class FileRequest {
	
	@NotNull
	@NotEmpty
	private String bookId;
	@NotNull
	private MultipartFile file;
	
	public FileRequest() {
		// TODO Auto-generated constructor stub
	}
	public FileRequest(@NotNull @NotEmpty String bookId, @NotNull @NotEmpty MultipartFile file) {
		super();
		this.bookId = bookId;
		this.file = file;
	}

	public String getBookId() {
		return bookId;
	}
	public MultipartFile getFile() {
		return file;
	}
	
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	// It's been 8 years...
	// https://github.com/spring-projects/spring-framework/issues/18012
	public void setBook_id(String bookId) {
		this.bookId = bookId;
	}
	
}
