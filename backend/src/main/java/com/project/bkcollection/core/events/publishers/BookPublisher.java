package com.project.bkcollection.core.events.publishers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.project.bkcollection.api.dtos.internal.BookDTO;
import com.project.bkcollection.api.dtos.request.BookRequest;
import com.project.bkcollection.api.dtos.request.BookUpdateRequest;
import com.project.bkcollection.core.events.BookDeleteEvent;
import com.project.bkcollection.core.events.BookInsertEvent;
import com.project.bkcollection.core.events.BookUpdateEvent;

@Component
public class BookPublisher {

    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public void publishInsert(BookRequest bookRequest, Long bookId) {
    	BookInsertEvent event = new BookInsertEvent(this, bookId, bookRequest);
        eventPublisher.publishEvent(event);
    }
    public void publishUpdate(BookUpdateRequest bookUpdateRequest, BookDTO bookDTO) {
    	BookUpdateEvent event = new BookUpdateEvent(this, bookDTO, bookUpdateRequest);
        eventPublisher.publishEvent(event);
    }
    public void publishDelete(BookDTO bookDTO) {
    	BookDeleteEvent event = new BookDeleteEvent(this, bookDTO);
        eventPublisher.publishEvent(event);
    }
    
}
