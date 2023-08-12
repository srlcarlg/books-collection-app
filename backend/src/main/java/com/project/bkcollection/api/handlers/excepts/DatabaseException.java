package com.project.bkcollection.api.handlers.excepts;

public class DatabaseException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public DatabaseException(String msg) {
		super(msg);		
	}
}
