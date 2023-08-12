package com.project.bkcollection.api.controllers.doc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.project.bkcollection.api.dtos.CustomPage;
import com.project.bkcollection.api.dtos.response.FavoriteResponse;
import com.project.bkcollection.api.handlers.models.ExceptionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

public @interface FavoriteDOC {
	
	@SecurityRequirement(name = "bearerToken")
	@Tag(name = "Favorite", description = "The Favorite API. "
			+ "Operations will be linked according to authenticated user.")
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface DOC_FavoriteController {}
	
	@Operation(
	summary = "Get all favorites books (paged)", 
	description = "**Allowed authorities: [ALL]**",
	responses = { 
		@ApiResponse(responseCode = "200", description = "OK", 
			content = @Content(mediaType = "application/json", 
			schema = @Schema(implementation = CustomPage.class)))
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_FindAllPaged {}
	
	@Operation(
	summary = "Get favorites books filtered",
	description = "**Allowed authorities: [ALL]**",
	responses = {
		@ApiResponse(responseCode = "200", description = "OK", 
			content = @Content(mediaType = "application/json", 
			schema = @Schema(implementation = FavoriteResponse.class))
		),
		@ApiResponse(responseCode = "404", description = "ID not found", 
		    content =  @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = ExceptionResponse.class))
		) 
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_FilterById {}
	
	@Operation(
	summary = "Favorite a book", 
	description = "**Allowed authorities: [ALL]**.",
	responses = { 
		@ApiResponse(responseCode = "200", description = "OK", 
		    content = @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = FavoriteResponse.class))
		),
		@ApiResponse(responseCode = "404", description = "ID not found.", 
	    	content =  @Content(mediaType = "application/json", 
	    	schema = @Schema(implementation = ExceptionResponse.class))
		)
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_Insert {}
		
	@Operation(
	summary = "Delete a favorite book",
	description = "**Allowed authorities: [ALL]**",
	responses = {
		@ApiResponse(responseCode = "204", description = "OK (No content)"),
		@ApiResponse(responseCode = "404", description = "ID not found.", 
	    	content =  @Content(mediaType = "application/json", 
	    	schema = @Schema(implementation = ExceptionResponse.class))
		)
	})
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface DOC_Delete {}
}
