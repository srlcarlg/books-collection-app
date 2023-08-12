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

import com.project.bkcollection.api.controllers.doc.CategoryDOC.DOC_CategoryController;
import com.project.bkcollection.api.controllers.doc.CategoryDOC.DOC_Delete;
import com.project.bkcollection.api.controllers.doc.CategoryDOC.DOC_FindAllPaged;
import com.project.bkcollection.api.controllers.doc.CategoryDOC.DOC_FindById;
import com.project.bkcollection.api.controllers.doc.CategoryDOC.DOC_Insert;
import com.project.bkcollection.api.controllers.doc.CategoryDOC.DOC_Update;
import com.project.bkcollection.api.dtos.CustomPage;
import com.project.bkcollection.api.dtos.request.CategoryRequest;
import com.project.bkcollection.api.dtos.response.CategoryResponse;
import com.project.bkcollection.api.services.CategoryService;
import com.project.bkcollection.core.BooksCollectionPermissions.isAuthenticated;
import com.project.bkcollection.core.BooksCollectionPermissions.isLibrarianOrAdmin;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/categories")
@DOC_CategoryController
public class CategoryController {
	
	@Autowired
	private CategoryService service;
	
	@DOC_FindAllPaged
	@isAuthenticated
	@GetMapping
	public CustomPage<CategoryResponse> findAllPaged(Pageable pageable) {
		return new CustomPage<CategoryResponse>(service.findAllPaged(pageable));
	}
	
	@DOC_FindById
	@isAuthenticated
	@GetMapping(path = "/{id}")
	public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
		CategoryResponse dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@DOC_Insert
	@isLibrarianOrAdmin
	@PostMapping
	public ResponseEntity<CategoryResponse> insert(@Valid @RequestBody CategoryRequest request) {
		CategoryResponse dto = service.insert(request);
		return ResponseEntity.ok().body(dto);
	}

	@DOC_Update
	@isLibrarianOrAdmin
	@PutMapping(path = "/{id}")
	public ResponseEntity<CategoryResponse> update(@Valid @RequestBody CategoryRequest request, @PathVariable Long id) {
		CategoryResponse dto = service.update(id, request);
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
