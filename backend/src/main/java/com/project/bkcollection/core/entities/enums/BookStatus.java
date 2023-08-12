package com.project.bkcollection.core.entities.enums;

public enum BookStatus {
	AVAILABLE(1, "Available"),
	MISSING(2, "Missing");
	
    private Integer id;
    private String description;
    
	private BookStatus(Integer id, String description) {
		this.id = id;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
