package com.project.bkcollection.api.mappers;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.project.bkcollection.api.dtos.request.CategoryRequest;
import com.project.bkcollection.api.dtos.response.BookLazyResponse;
import com.project.bkcollection.api.dtos.response.CategoryResponse;
import com.project.bkcollection.core.entities.Book;
import com.project.bkcollection.core.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
	
	CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
	
	@Mapping(target = "books", ignore = true)
    Category toEntity(CategoryRequest request);
	Set<Category> toEntity(Set<CategoryRequest> request);
	
    CategoryRequest toDTO(Category model);
    
    CategoryResponse toResponse(Category request);
    
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
