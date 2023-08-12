package com.project.bkcollection.api.dtos.response;

public class TokenResponse {

    private String access;
    private String refresh;
	
    public TokenResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TokenResponse(String access, String refresh) {
		super();
		this.access = access;
		this.refresh = refresh;
	}

	public String getAccess() {
		return access;
	}
	public String getRefresh() {
		return refresh;
	}

	public void setAccess(String access) {
		this.access = access;
	}
	public void setRefresh(String refresh) {
		this.refresh = refresh;
	}    
    
}
