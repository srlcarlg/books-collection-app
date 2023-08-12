package com.project.bkcollection.api.dtos.request;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.bkcollection.core.entities.enums.BookStatus;
import com.project.bkcollection.core.validators.BookValid;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonNaming(SnakeCaseStrategy.class)
@BookValid
public class BookRequest {
	    
    @NotNull
    @NotEmpty 
    private String title;
    
    private Integer publicationYear;
    private String description;
    
    @NotNull
    private BookStatus bookStatus;
    
    @NotNull
    @NotEmpty
    private Set<AuthorRequest> authors = new HashSet<>();    
    @NotNull
    @NotEmpty
    private Set<CategoryRequest> categories = new HashSet<>();    
    
    private String bookUrl;
    private String coverUrl;
    
    public BookRequest() {
		
	}
	public BookRequest(@NotNull @NotEmpty String title, Integer publicationYear, String description,
			@NotNull BookStatus bookStatus, @NotNull @NotEmpty Set<AuthorRequest> authors, @NotNull @NotEmpty Set<CategoryRequest> categories) {
		super();
		this.title = title;
		this.publicationYear = publicationYear;
		this.description = description;
		this.bookStatus = bookStatus;
		this.authors = authors;
		this.categories = categories;
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
	public Set<AuthorRequest> getAuthors() {
		return authors;
	}
	public Set<CategoryRequest> getCategories() {
		return categories;
	}
	public String getBookUrl() {
		return bookUrl;
	}
	public String getCoverUrl() {
		return coverUrl;
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
	public void setAuthors(Set<AuthorRequest> authors) {
		this.authors = authors;
	}
	public void setCategories(Set<CategoryRequest> categories) {
		this.categories = categories;
	}
	public void setBookUrl(String bookUrl) {
		this.bookUrl = bookUrl;
	}
	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}
}
