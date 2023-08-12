package com.project.bkcollection.core.entities.enums;

public enum TypeUser {
	CLIENT (1),
	LIBRARIAN (2),
	ADMIN (3);
	
    private Integer id;

	private TypeUser(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
       
}
