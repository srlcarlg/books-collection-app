package com.project.bkcollection.api.dtos.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.bkcollection.core.entities.enums.BookStatus;

@JsonNaming(SnakeCaseStrategy.class)
public class BookLazyResponse {
    
	private Long id;
    private String title;
    private Integer publicationYear;
    private String description;
    private BookStatus bookStatus;
    private String bookUrl;
    private String coverUrl;
    
    public BookLazyResponse() {
		
	}
    public BookLazyResponse(Long id, String title, Integer publicationYear,
			String description, BookStatus bookStatus, String bookUrl, String coverUrl) {
		super();
		this.id = id;
		this.title = title;
		this.publicationYear = publicationYear;
		this.description = description;
		this.bookStatus = bookStatus;
		this.bookUrl = bookUrl;
		this.coverUrl = coverUrl;
	}


	public Long getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public Integer getPublicationYear() {
		return publicationYear;
	}
	public String getDescription() {
		return description;
	}
	public BookStatus getBookStatus() {
		return bookStatus;
	}
	public String getBookUrl() {
		return bookUrl;
	}
	public String getCoverUrl() {
		return coverUrl;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setBookStatus(BookStatus bookStatus) {
		this.bookStatus = bookStatus;
	}
	public void setBookUrl(String bookUrl) {
		this.bookUrl = bookUrl;
	}
	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}
}
