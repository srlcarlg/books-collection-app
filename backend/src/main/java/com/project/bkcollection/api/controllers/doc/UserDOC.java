package com.project.bkcollection.api.controllers.doc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.project.bkcollection.api.dtos.CustomPage;
import com.project.bkcollection.api.dtos.response.UserResponse;
import com.project.bkcollection.api.dtos.response.UserUpdateResponse;
import com.project.bkcollection.api.handlers.models.ExceptionResponse;
import com.project.bkcollection.api.handlers.models.FieldErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

public @interface UserDOC {
	
	@Tag(name = "User", description = "The User API. "
			+ "Contains CRUD operations, mostly can only be performed by ADMIN.")
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface DOC_UserController {}

	@SecurityRequirement(name = "bearerToken")
	@Operation(
	summary = "Get all users (paged)", 
	description = "**Allowed authorities: [ADMIN]**",
	responses = { 
		@ApiResponse(
			responseCode = "200", description = "OK", 
			content = @Content(mediaType = "application/json", 
			schema = @Schema(implementation = CustomPage.class)))
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_FindAllPaged {}

	@SecurityRequirement(name = "bearerToken")
	@Operation(
	summary = "Get a user (by ID)",
	description = "**Allowed authorities: [ADMIN]**",
	responses = {
		@ApiResponse(
			responseCode = "200", description = "OK", 
			content = @Content(mediaType = "application/json", 
			schema = @Schema(implementation = UserResponse.class))
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
	summary = "Create a user", 
	description = "**Any request and permission for all**",
	responses = { 
		@ApiResponse(
		    responseCode = "200", description = "OK", 
		    content = @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = UserResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "Email already registered.", 
    	content =  @Content(mediaType = "application/json", 
    	schema = @Schema(implementation = ExceptionResponse.class))
		),
		@ApiResponse(responseCode = "422", description = "Validation Error.", 
	    	content =  @Content(mediaType = "application/json", 
	    	schema = @Schema(implementation = FieldErrorMessage.class))) 
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_Insert {}

	@SecurityRequirement(name = "bearerToken")
	@Operation(
	summary = "Update authenticated user",
	description = "**Allowed authorities: [ALL]**",
	responses = { 
		@ApiResponse(
		    responseCode = "200", description = "OK", 
		    content = @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = UserUpdateResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "Email already registered.", 
	    	content =  @Content(mediaType = "application/json", 
	    	schema = @Schema(implementation = ExceptionResponse.class))
		),
		@ApiResponse(responseCode = "422", description = "Validation Error.", 
	    	content =  @Content(mediaType = "application/json", 
	    	schema = @Schema(implementation = FieldErrorMessage.class))) 
		})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_Update {}

	@SecurityRequirement(name = "bearerToken")
	@Operation(
	summary = "Update user by ID",
	description = "**Allowed authorities: [ADMIN]**",
	responses = { 
		@ApiResponse(
		    responseCode = "200", description = "OK", 
		    content = @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = UserUpdateResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "Email already registered.", 
	    	content =  @Content(mediaType = "application/json", 
	    	schema = @Schema(implementation = ExceptionResponse.class))
		),
		@ApiResponse(responseCode = "422", description = "Validation Error.", 
	    	content =  @Content(mediaType = "application/json", 
	    	schema = @Schema(implementation = FieldErrorMessage.class))) 
		})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_UpdatebyID {}
	
	@SecurityRequirement(name = "bearerToken")
	@Operation(
	summary = "Delete a user (by ID)",
	description = "**Allowed authorities: [ADMIN]**",
	responses = @ApiResponse(responseCode = "204", description = "OK (No content)"))
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface DOC_Delete {}
}
