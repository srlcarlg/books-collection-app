package com.project.bkcollection.api.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AuthorRequest {
	
	private Long id;
    @NotNull
    @NotEmpty
    private String name;
    
    public AuthorRequest() {
		// TODO Auto-generated constructor stub
	}
	public AuthorRequest(Long id, @NotNull @NotEmpty String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
}
