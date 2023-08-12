package com.project.bkcollection.core.events;

import org.springframework.context.ApplicationEvent;

import com.project.bkcollection.api.dtos.request.BookRequest;

public class BookInsertEvent  extends ApplicationEvent {
    private static final long serialVersionUID = 1L;
    
    private Long bookId;
	private BookRequest bookRequest;
	
	public BookInsertEvent(Object source, Long bookId, BookRequest bookRequest) {
		super(source);
		this.bookId = bookId;
		this.bookRequest = bookRequest;
	}
	
	public Long getBookId() {
		return bookId;
	}
	public BookRequest getBookRequest() {
		return bookRequest;
	}
	
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
	public void setBookRequest(BookRequest bookRequest) {
		this.bookRequest = bookRequest;
	}
	
}
