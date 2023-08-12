package com.project.bkcollection.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bkcollection.api.dtos.response.UserResponse;
import com.project.bkcollection.api.services.MeService;
import com.project.bkcollection.core.BooksCollectionPermissions.isAuthenticated;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/me")
@SecurityRequirement(name = "bearerToken")
@Tag(name = "Me", description = "Just it.")
public class MeController {

    @Autowired
    private MeService service;
    
    @Operation(
	summary = "Show authenticated user information",
	responses = { 
		@ApiResponse(
			responseCode = "200", description = "OK", 
			content = @Content(mediaType = "application/json", 
			schema = @Schema(implementation = UserResponse.class)))
    })
	@isAuthenticated
    @GetMapping
    public UserResponse me() {
        return service.getAuthenticatedUser();
    }

}
