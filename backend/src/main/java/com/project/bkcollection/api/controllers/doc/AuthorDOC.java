package com.project.bkcollection.api.controllers.doc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.project.bkcollection.api.dtos.CustomPage;
import com.project.bkcollection.api.dtos.response.AuthorResponse;
import com.project.bkcollection.api.handlers.models.ExceptionResponse;
import com.project.bkcollection.api.handlers.models.ValidationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

public @interface AuthorDOC {
	
	@SecurityRequirement(name = "bearerToken")
	@Tag(name = "Author", description = "The Author API. "
			+ "Contains CRUD operations that can be performed depending on user authority.")
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface DOC_AuthorController {}
	
	@Operation(
	summary = "Get all authors and books[id] (paged)", 
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
	summary = "Get a author and books[id] (by ID)",
	description = "**Allowed authorities: [ALL]**",
	responses = {
		@ApiResponse(responseCode = "200", description = "OK", 
			content = @Content(mediaType = "application/json", 
			schema = @Schema(implementation = AuthorResponse.class))
		),
		@ApiResponse(responseCode = "404", description = "ID not found", 
		    content =  @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = ExceptionResponse.class))
		) 
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_FindById {}
	
	@Operation(
	summary = "Create a author", 
	description = "**Allowed authorities: [LIBRARIAN, ADMIN].**"
			+ "\n\n Only the *'name'* field must be sent.",
	responses = { 
		@ApiResponse(responseCode = "200", description = "OK", 
		    content = @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = AuthorResponse.class))
		),
		@ApiResponse(responseCode = "422", description = "Validation Error.", 
	    	content =  @Content(mediaType = "application/json", 
	    	schema = @Schema(implementation = ValidationResponse.class))
		), 
		@ApiResponse(responseCode = "400", description = "Author already registered.", 
			content =  @Content(mediaType = "application/json", 
			schema = @Schema(implementation = ExceptionResponse.class))
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
	summary = "Update a author (by ID)",
	description = "**Allowed authorities: [LIBRARIAN, ADMIN].**"
			+ "\n\n Only the *'name'* field must be sent.",
	responses = { 
		@ApiResponse(
		    responseCode = "200", description = "OK", 
		    content = @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = AuthorResponse.class))
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
	summary = "Delete a author (by ID)",
	description = "**Allowed authorities: [LIBRARIAN, ADMIN]**"
			+ "\n\n Cannot delete an Author that contains at least 1 book.",
	responses = {
		@ApiResponse(responseCode = "204", description = "OK (No content)"),
		@ApiResponse(responseCode = "404", description = "ID not found.", 
	    	content =  @Content(mediaType = "application/json", 
	    	schema = @Schema(implementation = ExceptionResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "Author contains at least 1 book.", 
	    	content =  @Content(mediaType = "application/json", 
	    	schema = @Schema(implementation = ExceptionResponse.class))
		),
		@ApiResponse(responseCode = "403", description = "Access Denied.", 
    	content =  @Content(mediaType = "application/json", 
    	schema = @Schema(implementation = ExceptionResponse.class))
		) 
	})
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface DOC_Delete {}
}
