package com.project.bkcollection.api.handlers.excepts;

import com.project.bkcollection.api.handlers.models.FieldErrorMessage;

public class CustomFieldErrorException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String fieldName;
	
	public CustomFieldErrorException(FieldErrorMessage fieldErrorMessage) {
		super(fieldErrorMessage.getMessage());
		fieldName = fieldErrorMessage.getFieldName();
	}
	
	public String getFieldName() {
		return fieldName;
	}

}
