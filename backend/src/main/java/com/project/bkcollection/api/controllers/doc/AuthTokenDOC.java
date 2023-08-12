package com.project.bkcollection.api.controllers.doc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.project.bkcollection.api.dtos.response.TokenResponse;
import com.project.bkcollection.api.handlers.models.ExceptionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

public @interface AuthTokenDOC {
	
	@Tag(name = "Auth", description = "The Auth API with JJWT API.")
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface DOC_AuthTokenController {}

	@Operation(
	summary = "Autheticate and receive Access/Refresh token",
	responses = { 
		@ApiResponse(
			responseCode = "200", description = "OK", 
			content = @Content(mediaType = "application/json", 
			schema = @Schema(implementation = TokenResponse.class))
		),
		@ApiResponse(
			responseCode = "401", description = "Username does not exist or password is invalid.", 
		    content =  @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = ExceptionResponse.class))
		) 
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_Authenticate {}

	@Operation(
	summary = "reAutheticate with Refresh token",
	responses = {
		@ApiResponse(
			responseCode = "200", description = "OK", 
			content = @Content(mediaType = "application/json", 
			schema = @Schema(implementation = TokenResponse.class))
		),
		@ApiResponse(
			responseCode = "401", description = "Invalid token! or \n\n Invalid Bearer token! signing key cannot be null.", 
		    content =  @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = ExceptionResponse.class))
		) 
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_reAutheticate {}
	
	@Operation(
	summary = "Logout",
	responses = { 
		@ApiResponse(
			responseCode = "205", description = "OK (Reset Content)", 
		    content = @Content(mediaType = "application/json"))
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_Logout {}
}
