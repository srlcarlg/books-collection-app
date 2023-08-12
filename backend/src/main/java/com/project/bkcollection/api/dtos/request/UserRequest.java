package com.project.bkcollection.api.dtos.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.bkcollection.core.entities.enums.TypeUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonNaming(SnakeCaseStrategy.class)
public class UserRequest {
	
    @NotNull
    @NotEmpty 
    private String name;
    
    @NotNull
    private TypeUser typeUser;
    
    @NotNull
    @NotEmpty 
    @Email(message = "Must be a valid email!")
    private String email;
    
    @NotNull
    @NotEmpty 
    private String password;
    
    @NotNull
    @NotEmpty 
    private String confirmationPassword;
    
    public UserRequest() {
		// TODO Auto-generated constructor stub
	}
	public UserRequest(@NotNull @NotEmpty String name, @NotNull TypeUser typeUser,
			@NotNull @NotEmpty @Email(message = "Must be a valid email!") String email,
			@NotNull @NotEmpty String password, @NotNull @NotEmpty String confirmationPassword) {
		super();
		this.name = name;
		this.typeUser = typeUser;
		this.email = email;
		this.password = password;
		this.confirmationPassword = confirmationPassword;
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
	public String getPassword() {
		return password;
	}
	public String getConfirmationPassword() {
		return confirmationPassword;
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
	public void setPassword(String password) {
		this.password = password;
	}
    public void setConfirmationPassword(String confirmationPassword) {
		this.confirmationPassword = confirmationPassword;
	}
}
