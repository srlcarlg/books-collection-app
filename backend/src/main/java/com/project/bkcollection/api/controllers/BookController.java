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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bkcollection.api.controllers.doc.BookDOC.DOC_BookController;
import com.project.bkcollection.api.controllers.doc.BookDOC.DOC_Delete;
import com.project.bkcollection.api.controllers.doc.BookDOC.DOC_FindAllPaged;
import com.project.bkcollection.api.controllers.doc.BookDOC.DOC_FindById;
import com.project.bkcollection.api.controllers.doc.BookDOC.DOC_Insert;
import com.project.bkcollection.api.controllers.doc.BookDOC.DOC_Update;
import com.project.bkcollection.api.dtos.CustomPage;
import com.project.bkcollection.api.dtos.request.BookRequest;
import com.project.bkcollection.api.dtos.request.BookUpdateRequest;
import com.project.bkcollection.api.dtos.response.BookResponse;
import com.project.bkcollection.api.services.BookService;
import com.project.bkcollection.core.BooksCollectionPermissions.isAuthenticated;
import com.project.bkcollection.core.BooksCollectionPermissions.isLibrarianOrAdmin;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/books")
@DOC_BookController
public class BookController {
	
	@Autowired
	private BookService service;
	
	@DOC_FindAllPaged
	@isAuthenticated
	@GetMapping
	public CustomPage<BookResponse> findAllPaged(@RequestParam(required = false) String title, Pageable pageable) {
		return new CustomPage<BookResponse>(
			title == null ? service.findAllPaged(pageable) : service.findAllContainTitlePaged(title, pageable)
		);
	}
	
	@DOC_FindById
	@isAuthenticated
	@GetMapping(path = "/{id}")
	public ResponseEntity<BookResponse> findById(@PathVariable Long id) {
		BookResponse dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@DOC_Insert
	@isLibrarianOrAdmin
	@PostMapping
	public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
		BookResponse dto = service.insert(request);
		return ResponseEntity.ok().body(dto);
	}

	@DOC_Update
	@isLibrarianOrAdmin
	@PutMapping(path = "/{id}")
	public ResponseEntity<BookResponse> update(@Valid @RequestBody BookUpdateRequest request, @PathVariable Long id) {
		BookResponse dto = service.update(id, request);
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
