package com.project.bkcollection.core.events.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.project.bkcollection.api.dtos.internal.BookDTO;
import com.project.bkcollection.api.dtos.request.BookRequest;
import com.project.bkcollection.api.dtos.request.BookUpdateRequest;
import com.project.bkcollection.core.events.BookDeleteEvent;
import com.project.bkcollection.core.events.BookInsertEvent;
import com.project.bkcollection.core.events.BookUpdateEvent;
import com.project.bkcollection.core.services.storage.adapters.StorageService;

@Component
public class BookListener {
		
	@Autowired 
	private StorageService storageService;
	
	@EventListener
    public void handleBookInsertEvent(BookInsertEvent event) {
		BookRequest bookRequest = event.getBookRequest();
		Long bookId = event.getBookId();
		waitForSeconds(2);
		storageService.saveFromUrl(bookRequest, bookId);		
    }
	@EventListener
    public void handleBookUpdateEvent(BookUpdateEvent event) {
		BookUpdateRequest bookRequest = event.getBookRequest();
		BookDTO bookDTO = event.getBookDTO();
		waitForSeconds(2);
		storageService.update(bookRequest, bookDTO);		
    }
	@EventListener
    public void handleBookDeleteEvent(BookDeleteEvent event) {
		BookDTO bookId = event.getbookDTO();
		storageService.delete(bookId);		
    }
	
	private void waitForSeconds(Integer seconds) {
		try {Thread.sleep((long)(seconds * 1000));} catch (InterruptedException e) {}
	}
}
