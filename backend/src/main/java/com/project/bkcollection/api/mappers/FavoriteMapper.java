package com.project.bkcollection.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.project.bkcollection.api.dtos.response.FavoriteResponse;
import com.project.bkcollection.core.entities.Favorite;
import com.project.bkcollection.core.entities.FileInfo;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {
	
	FavoriteMapper INSTANCE = Mappers.getMapper(FavoriteMapper.class);

    @Mapping(source = "book.bookFile", target = "book.bookUrl", qualifiedByName = "fileInfoToUrl")
    @Mapping(source = "book.coverFile", target = "book.coverUrl", qualifiedByName = "fileInfoToUrl")
    FavoriteResponse toResponse(Favorite request);
    
    @Named("fileInfoToUrl")
    public static String fileInfoToUrl(FileInfo fileInfo) {
    	if (fileInfo == null) {
    		return "";
    	}
        return fileInfo.getUrl() == null ? "" : fileInfo.getUrl() ;
    }
}
