package com.project.bkcollection.api.controllers.doc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.project.bkcollection.api.handlers.models.ExceptionResponse;
import com.project.bkcollection.api.handlers.models.FieldErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

public @interface StorageDOC {

	@Tag(name = "Storage", description = "The Storage API for PDF files")
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface DOC_StorageController {}

	@Operation(
	summary = "Search a file",
	description = "",
	responses = { 
		@ApiResponse(
			responseCode = "200", description = "return a PDF or Image file", 
			content = @Content(mediaType = "application/")
		),
		@ApiResponse(
			responseCode = "404", description = "File 'x' not found.", 
		    content =  @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = ExceptionResponse.class))
		),
		@ApiResponse(
			responseCode = "417", description = "If something really rare happens, like IOException", 
		    content =  @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = ExceptionResponse.class))
		) 
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_SearchFile {}

	@SecurityRequirement(name = "bearerToken")
	@Operation(
	summary = "Upload a PDF file",

	description = "**Allowed authorities: [LIBRARIAN, ADMIN].**" + "\n\n The first page will be used as the Cover.",
	responses = { 
		@ApiResponse(
			responseCode = "200", description = "OK", 
			content = @Content(mediaType = "application/json",
			schema = @Schema(example = "/files/***")) 
		),
		@ApiResponse(
			responseCode = "422", description = "Only .pdf is supported. \n\n bookId should be a number.",
		    content =  @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = FieldErrorMessage.class))
		),
		@ApiResponse(
			responseCode = "417", description = "If something really rare happens, like IOException", 
		    content =  @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = ExceptionResponse.class))
		) 
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_InsertFile {}

	@SecurityRequirement(name = "bearerToken")
	@Operation(
	summary = "Delete PDF from Book",
	description = "**Allowed authorities: [LIBRARIAN, ADMIN].**",
	responses = { 
		@ApiResponse(
			responseCode = "204", description = "OK (No Content)", 
		    content = @Content(mediaType = "application/json")),
		@ApiResponse(
			responseCode = "404", description = "ID not found", 
		    content =  @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = ExceptionResponse.class))
		) 
	})
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DOC_DeleteFile {}
}
