package com.project.bkcollection.api.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class RefreshTokenRequest {

    @NotNull
    @NotEmpty
    private String refresh;

	public RefreshTokenRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RefreshTokenRequest(@NotNull @NotEmpty String refresh) {
		super();
		this.refresh = refresh;
	}

	public String getRefresh() {
		return refresh;
	}

	public void setRefresh(String refresh) {
		this.refresh = refresh;
	}
    
}

