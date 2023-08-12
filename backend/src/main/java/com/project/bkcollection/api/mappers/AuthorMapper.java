package com.project.bkcollection.api.mappers;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.project.bkcollection.api.dtos.request.AuthorRequest;
import com.project.bkcollection.api.dtos.response.AuthorResponse;
import com.project.bkcollection.api.dtos.response.BookLazyResponse;
import com.project.bkcollection.core.entities.Author;
import com.project.bkcollection.core.entities.Book;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
	
	AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);
	
	@Mapping(target = "books", ignore = true)
    Author toEntity(AuthorRequest request);
	Set<Author> toEntity(Set<AuthorRequest> request);
	
    AuthorRequest toDTO(Author model);
    
    AuthorResponse toResponse(Author request);
    
    default BookLazyResponse bookToBookLazyResponse(Book book) {
        if ( book == null ) {
            return null;
        }

        BookLazyResponse bookLazyResponse = new BookLazyResponse();

        bookLazyResponse.setBookStatus( book.getBookStatus() );
        bookLazyResponse.setDescription( book.getDescription() );
        bookLazyResponse.setId( book.getId() );
        bookLazyResponse.setPublicationYear( book.getPublicationYear() );
        bookLazyResponse.setTitle( book.getTitle());
        bookLazyResponse.setBookUrl( book.getBookFile() == null ? "" : book.getBookFile().getUrl() );
        bookLazyResponse.setCoverUrl( book.getCoverFile() == null ? "" :book.getCoverFile().getUrl() );

        return bookLazyResponse;
    }
}
