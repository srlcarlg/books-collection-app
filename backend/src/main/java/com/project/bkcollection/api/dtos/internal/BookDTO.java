package com.project.bkcollection.api.dtos.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.project.bkcollection.core.entities.Author;
import com.project.bkcollection.core.entities.Category;
import com.project.bkcollection.core.entities.Favorite;
import com.project.bkcollection.core.entities.FileInfo;
import com.project.bkcollection.core.entities.enums.BookStatus;

public class BookDTO {
	
    private Long id;
    private String title;
    
    private Integer publicationYear;
    private String description;
    private BookStatus bookStatus;
 
    private Set<Author> authors = new HashSet<>();
    private Set<Category> categories = new HashSet<>();
    private List<Favorite> favorites = new ArrayList<>();
    
    private FileInfo bookFile;
    private FileInfo coverFile;
    
    public BookDTO() {
    	
	}
	public BookDTO(Long id, String title, Integer publicationYear, String description, BookStatus bookStatus,
			Set<Author> authors, Set<Category> categories, List<Favorite> favorites, FileInfo bookFile,
			FileInfo coverFile) {
		super();
		this.id = id;
		this.title = title;
		this.publicationYear = publicationYear;
		this.description = description;
		this.bookStatus = bookStatus;
		this.authors = authors;
		this.categories = categories;
		this.favorites = favorites;
		this.bookFile = bookFile;
		this.coverFile = coverFile;
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
	public Set<Author> getAuthors() {
		return authors;
	}
	public Set<Category> getCategories() {
		return categories;
	}
	public List<Favorite> getFavorites() {
		return favorites;
	}
	public FileInfo getBookFile() {
		return bookFile;
	}
	public FileInfo getCoverFile() {
		return coverFile;
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
	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}
	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	public void setFavorites(List<Favorite> favorites) {
		this.favorites = favorites;
	}
	public void setBookFile(FileInfo bookFile) {
		this.bookFile = bookFile;
	}
	public void setCoverFile(FileInfo coverFile) {
		this.coverFile = coverFile;
	}
	
	@Override
	public String toString() {
		return "BookDTO [id=" + id + ", title=" + title + ", publicationYear=" + publicationYear + ", description="
				+ description + ", bookStatus=" + bookStatus + ", authors=" + authors + ", categories=" + categories
				+ ", favorites=" + favorites + ", bookFile=" + bookFile + ", coverFile=" + coverFile + "]";
	}
	
	
}
