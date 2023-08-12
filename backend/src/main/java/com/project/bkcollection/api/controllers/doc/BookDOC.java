package com.project.bkcollection.api.controllers.doc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.project.bkcollection.api.dtos.CustomPage;
import com.project.bkcollection.api.dtos.response.BookResponse;
import com.project.bkcollection.api.handlers.models.ExceptionResponse;
import com.project.bkcollection.api.handlers.models.ValidationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

public @interface BookDOC {
	
	@SecurityRequirement(name = "bearerToken")
	@Tag(name = "Book", description = "The Book API. "
			+ "Contains CRUD operations that can be performed depending on user authority.")
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface DOC_BookController {}
	
	@Operation(
	summary = "Get all books (paged)", 
	description = "**Allowed authorities: [ALL]**",
	responses = { 
		@ApiResponse(
			responseCode = "200", description = "OK", 
			content = @Content(mediaType = "application/json", 
			schema = @Schema(implementation = CustomPage.class)))
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_FindAllPaged {}
	
	@Operation(
	summary = "Get a book (by ID)",
	description = "**Allowed authorities: [ALL]**",
	responses = {
		@ApiResponse(
			responseCode = "200", description = "OK", 
			content = @Content(mediaType = "application/json", 
			schema = @Schema(implementation = BookResponse.class))
		),
		@ApiResponse(
			responseCode = "404", description = "ID not found", 
		    content =  @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = ExceptionResponse.class))
		) 
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_FindById {}
	
	@Operation(
	summary = "Create a book", 
	description = "**Allowed authorities: [LIBRARIAN, ADMIN].**" + 
			"\n\n Only fields *'title', 'book_status', 'authors', 'categories'* is mandatory." +
			"\n\n Authors/Categories must be added via ID." + 
			"\n\n URL media type from *'book_url' or 'cover_url'* **must be** *pdf or jpg/jpeg/png*, respectively." +
			"\n\n As the *URL Processing Event* starts 2s after adding the book, **if the media type is not supported or unreachable**, an 'inner exception' will be thrown and the content of the URL will not be processed.",
	responses = { 
		@ApiResponse(
		    responseCode = "200", description = "OK", 
		    content = @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = BookResponse.class))
		),
		@ApiResponse(responseCode = "422", description = "Validation Error.", 
	    	content =  @Content(mediaType = "application/json", 
	    	schema = @Schema(implementation = ValidationResponse.class))
		),
		@ApiResponse(responseCode = "403", description = "Access Denied.", 
    	content =  @Content(mediaType = "application/json", 
    	schema = @Schema(implementation = ExceptionResponse.class))
		) 
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_Insert {}
	
	@Operation(
	summary = "Update a book (by ID)",
	description = "**Allowed authorities: [LIBRARIAN, ADMIN]**" +
			"\n\n All fields are optional." + 
			"\n\n Authors/Categories must be added via ID." + 
			"\n\n If the submitted *'title' or 'authors'* differs from database, the PDF and cover files will be renamed/moved according to the new *'title' or 'author'.*"
			+ "\n\n The *URL media type and URL Processing Event* description are the same of *'Create a book'*",
	responses = { 
		@ApiResponse(
		    responseCode = "200", description = "OK", 
		    content = @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = BookResponse.class))
		),
		@ApiResponse(responseCode = "404", description = "ID not found.", 
	    	content =  @Content(mediaType = "application/json", 
	    	schema = @Schema(implementation = ExceptionResponse.class))
		),
		@ApiResponse(responseCode = "422", description = "Validation Error.", 
	    	content =  @Content(mediaType = "application/json", 
	    	schema = @Schema(implementation = ValidationResponse.class))
		),
		@ApiResponse(responseCode = "403", description = "Access Denied.", 
    	content =  @Content(mediaType = "application/json", 
    	schema = @Schema(implementation = ExceptionResponse.class))
		)  
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_Update {}
	
	@Operation(
	summary = "Delete a book (by ID)",
	description = "**Allowed authorities: [LIBRARIAN, ADMIN]**" + 
			"\n\n PDF and cover files will also be removed.",
	responses = {
			@ApiResponse(responseCode = "204", description = "OK (No content)"),
			@ApiResponse(responseCode = "403", description = "Access Denied.", 
	    	content =  @Content(mediaType = "application/json", 
	    	schema = @Schema(implementation = ExceptionResponse.class))
			) 
	})
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface DOC_Delete {}
}
