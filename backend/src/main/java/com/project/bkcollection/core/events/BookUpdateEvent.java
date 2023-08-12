package com.project.bkcollection.core.events;

import org.springframework.context.ApplicationEvent;

import com.project.bkcollection.api.dtos.internal.BookDTO;
import com.project.bkcollection.api.dtos.request.BookUpdateRequest;

public class BookUpdateEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;
    
    private BookDTO bookDTO;
	private BookUpdateRequest bookRequest;
	
	public BookUpdateEvent(Object source, BookDTO BookDTO, BookUpdateRequest bookRequest) {
		super(source);
		this.bookDTO = BookDTO;
		this.bookRequest = bookRequest;
	}
	
	public BookDTO getBookDTO() {
		return bookDTO;
	}
	public BookUpdateRequest getBookRequest() {
		return bookRequest;
	}
	
	public void setBookDTO(BookDTO bookDTO) {
		this.bookDTO = bookDTO;
	}
	public void setBookRequest(BookUpdateRequest bookRequest) {
		this.bookRequest = bookRequest;
	}
	
}
