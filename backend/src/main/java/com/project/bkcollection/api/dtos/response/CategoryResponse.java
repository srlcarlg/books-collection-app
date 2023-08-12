package com.project.bkcollection.api.dtos.response;

import java.util.HashSet;
import java.util.Set;

public class CategoryResponse {

	private Long id;
    private String name;
    private Set<BookLazyResponse> books = new HashSet<>();
    
    public CategoryResponse() {
		// TODO Auto-generated constructor stub
	}
	public CategoryResponse(Long id, String name, Set<BookLazyResponse> books) {
		super();
		this.id = id;
		this.name = name;
		this.books = books;
	}
	
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public Set<BookLazyResponse> getBooks() {
		return books;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setBooks(Set<BookLazyResponse> books) {
		this.books = books;
	}
}
