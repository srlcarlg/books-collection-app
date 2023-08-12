package com.project.bkcollection.api.dtos.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@JsonNaming(SnakeCaseStrategy.class)
public class FavoriteResponse {
    
	private BookLazyResponse book; 
    
    public FavoriteResponse() {
		
	}
	public FavoriteResponse(BookLazyResponse book) {
		super();
		this.book = book;
	}
	
	public BookLazyResponse getBook() {
		return book;
	}
	public void setBook(BookLazyResponse book) {
		this.book = book;
	}
		
}
