	package com.project.bkcollection.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bkcollection.api.controllers.doc.AuthorDOC.DOC_AuthorController;
import com.project.bkcollection.api.controllers.doc.AuthorDOC.DOC_Delete;
import com.project.bkcollection.api.controllers.doc.AuthorDOC.DOC_FindAllPaged;
import com.project.bkcollection.api.controllers.doc.AuthorDOC.DOC_FindById;
import com.project.bkcollection.api.controllers.doc.AuthorDOC.DOC_Insert;
import com.project.bkcollection.api.controllers.doc.AuthorDOC.DOC_Update;
import com.project.bkcollection.api.dtos.CustomPage;
import com.project.bkcollection.api.dtos.request.AuthorRequest;
import com.project.bkcollection.api.dtos.response.AuthorResponse;
import com.project.bkcollection.api.services.AuthorService;
import com.project.bkcollection.core.BooksCollectionPermissions.isAuthenticated;
import com.project.bkcollection.core.BooksCollectionPermissions.isLibrarianOrAdmin;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/authors")
@DOC_AuthorController
public class AuthorController {

	@Autowired
	private AuthorService service;
	
	@DOC_FindAllPaged
	@isAuthenticated
	@GetMapping
	public CustomPage<AuthorResponse> findAllPaged(Pageable pageable) {
		return new CustomPage<AuthorResponse>(service.findAllPaged(pageable));
	}
	
	@DOC_FindById
	@isAuthenticated
	@GetMapping(path = "/{id}")
	public ResponseEntity<AuthorResponse> findById(@PathVariable Long id) {
		AuthorResponse dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@DOC_Insert
	@isLibrarianOrAdmin
	@PostMapping
	public ResponseEntity<AuthorResponse> insert(@Valid @RequestBody AuthorRequest request) {
		AuthorResponse dto = service.insert(request);
		return ResponseEntity.ok().body(dto);
	}
	
	@DOC_Update
	@isLibrarianOrAdmin
	@PutMapping(path = "/{id}")
	public ResponseEntity<AuthorResponse> update(@Valid @RequestBody AuthorRequest request, @PathVariable Long id) {
		AuthorResponse dto = service.update(id, request);
		return ResponseEntity.ok().body(dto);
	}
	
	@DOC_Delete
	@isLibrarianOrAdmin
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
