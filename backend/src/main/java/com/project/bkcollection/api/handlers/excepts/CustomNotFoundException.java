package com.project.bkcollection.api.handlers.excepts;

public class CustomNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public CustomNotFoundException(String msg) {
		super(msg);		
	}
	
	public CustomNotFoundException() {
		super("ID not found!");		
	}
}
