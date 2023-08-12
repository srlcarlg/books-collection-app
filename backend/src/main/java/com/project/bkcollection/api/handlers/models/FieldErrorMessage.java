package com.project.bkcollection.api.handlers.models;

import java.io.Serializable;

public class FieldErrorMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String fieldName;
	private String message;
	
	public FieldErrorMessage() {
	}

	public FieldErrorMessage(String fieldName, String message) {
		super();
		this.fieldName = fieldName;
		this.message = message;
	}

	public String getFieldName() {
		return fieldName;
	}
	public String getMessage() {
		return message;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
