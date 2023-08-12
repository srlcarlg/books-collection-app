package com.project.bkcollection.core.events;

import org.springframework.context.ApplicationEvent;

import com.project.bkcollection.api.dtos.internal.BookDTO;

public class BookDeleteEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;
    
    private BookDTO bookDTO;
	
	public BookDeleteEvent(Object source, BookDTO bookDTO) {
		super(source);
		this.bookDTO = bookDTO;
	}
	
	public BookDTO getbookDTO() {
		return bookDTO;
	}
}
