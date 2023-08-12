package com.project.bkcollection.api.dtos.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public class UserUpdateResponse {
	
	private String message;
	private Boolean withPassword;
	
	public UserUpdateResponse() {
		// TODO Auto-generated constructor stub
	}
	public UserUpdateResponse(String message, Boolean withPassword) {
		super();
		this.message = message;
		this.withPassword = withPassword;
	}
	
	public String getMessage() {
		return message;
	}
	public Boolean getWithPassword() {
		return withPassword;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	public void setWithPassword(Boolean withPassword) {
		this.withPassword = withPassword;
	}
	
}
