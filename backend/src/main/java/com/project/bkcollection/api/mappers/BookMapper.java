package com.project.bkcollection.api.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.project.bkcollection.api.dtos.internal.BookDTO;
import com.project.bkcollection.api.dtos.request.BookRequest;
import com.project.bkcollection.api.dtos.response.BookResponse;
import com.project.bkcollection.core.entities.Author;
import com.project.bkcollection.core.entities.Book;
import com.project.bkcollection.core.entities.Category;
import com.project.bkcollection.core.entities.FileInfo;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

	BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);
	
	@Mapping(target = "favorites", ignore = true)
	@Mapping(target = "id", ignore = true)
    Book toEntity(BookRequest bookRequest);
	
    BookDTO toDTO(Book book);
    
    @Mapping(source = "bookFile", target = "bookUrl", qualifiedByName = "fileInfoToUrl")
    @Mapping(source = "coverFile", target = "coverUrl", qualifiedByName = "fileInfoToUrl")
    BookResponse toResponse(Book book);

    @Named("fileInfoToUrl")
    public static String fileInfoToUrl(FileInfo fileInfo) {
    	if (fileInfo == null) {
    		return "";
    	}
        return fileInfo.getUrl() == null ? "" : fileInfo.getUrl();
    }
   
    @Named("authorsSetToString")
    public static Set<String> authorsSetToString(Set<Author> set) {
        return set.stream().map(x -> x.getName()).collect(Collectors.toSet());
    }    
    @Named("categoriesSetToString")
    public static Set<String> categoriesSetToString(Set<Category> set) {
        return set.stream().map(x -> x.getName()).collect(Collectors.toSet());
    }
}
