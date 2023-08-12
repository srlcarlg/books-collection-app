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

import com.project.bkcollection.api.controllers.doc.UserDOC.DOC_Delete;
import com.project.bkcollection.api.controllers.doc.UserDOC.DOC_FindAllPaged;
import com.project.bkcollection.api.controllers.doc.UserDOC.DOC_FindById;
import com.project.bkcollection.api.controllers.doc.UserDOC.DOC_Insert;
import com.project.bkcollection.api.controllers.doc.UserDOC.DOC_Update;
import com.project.bkcollection.api.controllers.doc.UserDOC.DOC_UpdatebyID;
import com.project.bkcollection.api.controllers.doc.UserDOC.DOC_UserController;
import com.project.bkcollection.api.dtos.CustomPage;
import com.project.bkcollection.api.dtos.request.UserRequest;
import com.project.bkcollection.api.dtos.request.UserUpdateRequest;
import com.project.bkcollection.api.dtos.response.UserResponse;
import com.project.bkcollection.api.dtos.response.UserUpdateResponse;
import com.project.bkcollection.api.services.UserService;
import com.project.bkcollection.core.BooksCollectionPermissions.isAdmin;
import com.project.bkcollection.core.BooksCollectionPermissions.isAuthenticated;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/users")
@DOC_UserController
public class UserController {
	
	@Autowired
	private UserService service;
	
	@DOC_FindAllPaged
	@isAdmin
	@GetMapping
	public CustomPage<UserResponse> findAllPaged(Pageable pageable) {
		return new CustomPage<UserResponse>(service.findAllPaged(pageable));
	}
	
	@DOC_FindById
	@isAdmin
	@GetMapping(path = "/{id}")
	public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
		UserResponse dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@DOC_Insert
	@PostMapping
	public ResponseEntity<UserResponse> insert(@Valid @RequestBody UserRequest request) {
		UserResponse dto = service.insert(request);
		return ResponseEntity.ok().body(dto);
	}
	
	@DOC_Update
	@isAuthenticated
	@PutMapping
	public ResponseEntity<UserUpdateResponse> update(@Valid @RequestBody UserUpdateRequest request) {
		UserUpdateResponse dto = service.update(request);
		return ResponseEntity.ok().body(dto);
	}
	
	@DOC_UpdatebyID
	@isAdmin
	@PutMapping(path = "/{id}")
	public ResponseEntity<UserResponse> updatebyId(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
		UserResponse dto = service.updatebyId(id, request);
		return ResponseEntity.ok().body(dto);
	}
	
	@DOC_Delete
	@isAdmin
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
