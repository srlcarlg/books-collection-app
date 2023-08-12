package com.project.bkcollection.api.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TokenRequest {

    @NotNull
    @NotEmpty
    @Email
    @Size(max = 255)
    private String email;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String password;

	public TokenRequest() {
		
	}
	public TokenRequest(@NotNull @NotEmpty @Email @Size(max = 255) String email,
			@NotNull @NotEmpty @Size(max = 255) String password) {
		super();
		this.email = email;
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
        
}