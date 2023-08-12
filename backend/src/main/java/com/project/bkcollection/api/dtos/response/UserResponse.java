package com.project.bkcollection.api.dtos.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.bkcollection.core.entities.enums.TypeUser;


@JsonNaming(SnakeCaseStrategy.class)
public class UserResponse {
	
	private Long id;
    private String name;
    private TypeUser typeUser;
    private String email;

    public UserResponse() {
		// TODO Auto-generated constructor stub
	}   
	public UserResponse(Long id, String name, TypeUser typeUser, String email) {
		super();
		this.id = id;
		this.name = name;
		this.typeUser = typeUser;
		this.email = email;
	}

	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public TypeUser getTypeUser() {
		return typeUser;
	}
	public String getEmail() {
		return email;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTypeUser(TypeUser typeUser) {
		this.typeUser = typeUser;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
