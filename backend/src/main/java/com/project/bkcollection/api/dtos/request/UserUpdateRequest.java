package com.project.bkcollection.api.dtos.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;

@JsonNaming(SnakeCaseStrategy.class)
public class UserUpdateRequest {
	
    private String name;
    @Email(message = "Must be a valid email!")
    private String email;
    private String password;
    private String newPassword;
    private String confirmationPassword;
    
    public UserUpdateRequest() {
		// TODO Auto-generated constructor stub
	}
	public UserUpdateRequest(String name, @Email(message = "Must be a valid email!") String email, String password,
			String newPassword, String confirmationPassword) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.newPassword = newPassword;
		this.confirmationPassword = confirmationPassword;
	}
	
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public String getConfirmationPassword() {
		return confirmationPassword;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public void setConfirmationPassword(String confirmationPassword) {
		this.confirmationPassword = confirmationPassword;
	}
    
}
