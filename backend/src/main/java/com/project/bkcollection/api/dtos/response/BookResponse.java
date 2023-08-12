package com.project.bkcollection.api.dtos.response;

import java.util.Set;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.bkcollection.api.dtos.request.AuthorRequest;
import com.project.bkcollection.api.dtos.request.CategoryRequest;
import com.project.bkcollection.core.entities.enums.BookStatus;


@JsonNaming(SnakeCaseStrategy.class)
public class BookResponse {
    
	private Long id;
    private String title;
    private Integer publicationYear;
    private Set<AuthorRequest> authors;
    private Set<CategoryRequest> categories;
    private String description;
    private BookStatus bookStatus;
    private String bookUrl;
    private String coverUrl;
    
    public BookResponse() {
		
	}
    public BookResponse(Long id, String title, Integer publicationYear, Set<AuthorRequest> authors, Set<CategoryRequest> categories,
			String description, BookStatus bookStatus, String bookUrl, String coverUrl) {
		super();
		this.id = id;
		this.title = title;
		this.publicationYear = publicationYear;
		this.authors = authors;
		this.categories = categories;
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
	public Set<AuthorRequest> getAuthors() {
		return authors;
	}
	public Set<CategoryRequest> getcategories() {
		return categories;
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
	public void setAuthors(Set<AuthorRequest> authors) {
		this.authors = authors;
	}
	public void setcategories(Set<CategoryRequest> categories) {
		this.categories = categories;
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
