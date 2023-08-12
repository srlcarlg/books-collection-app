package com.project.bkcollection.api.handlers.models;

import java.util.ArrayList;
import java.util.List;

public class ValidationResponse extends ExceptionResponse {
	private static final long serialVersionUID = 1L;
	
	private List<FieldErrorMessage> errors = new ArrayList<>(); 
	
	public List<FieldErrorMessage> getErrors() {
		return errors;
	}
	
	public void addError(String fieldName, String message) {
		errors.add(new FieldErrorMessage(fieldName, message));
	}
}
