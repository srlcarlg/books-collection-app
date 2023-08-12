package com.project.bkcollection.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bkcollection.api.controllers.doc.FavoriteDOC.DOC_Delete;
import com.project.bkcollection.api.controllers.doc.FavoriteDOC.DOC_FavoriteController;
import com.project.bkcollection.api.controllers.doc.FavoriteDOC.DOC_FilterById;
import com.project.bkcollection.api.controllers.doc.FavoriteDOC.DOC_FindAllPaged;
import com.project.bkcollection.api.controllers.doc.FavoriteDOC.DOC_Insert;
import com.project.bkcollection.api.dtos.CustomPage;
import com.project.bkcollection.api.dtos.response.BookResponse;
import com.project.bkcollection.api.dtos.response.FavoriteResponse;
import com.project.bkcollection.api.services.FavoriteService;
import com.project.bkcollection.core.BooksCollectionPermissions.isAuthenticated;

@RestController
@RequestMapping("api/favorites")
@DOC_FavoriteController
public class FavoriteController {
	
	@Autowired
	private FavoriteService service;
	
	@DOC_FindAllPaged
	@isAuthenticated
	@GetMapping
	public CustomPage<Object> findAllPaged(Pageable pageable) {
		return new CustomPage<Object>(service.findAllPaged(pageable));
	}
	
	@DOC_FilterById
	@isAuthenticated
	@GetMapping("/{dynamicId}")
	public ResponseEntity<Object> filterById(@PathVariable Long dynamicId, @RequestParam(defaultValue = "false") Boolean isCategory) {
		List<BookResponse> dto = service.filterById(dynamicId, isCategory);
		return ResponseEntity.ok().body(dto);
	}
	
	@DOC_Insert
	@isAuthenticated
	@PostMapping("/{bookId}")
	public ResponseEntity<FavoriteResponse> insert(@PathVariable Long bookId) {
		FavoriteResponse dto = service.insert(bookId);
		return ResponseEntity.ok().body(dto);
	}
	
	@DOC_Delete
	@isAuthenticated
	@DeleteMapping(path = "/{bookId}")
	public ResponseEntity<Void> delete(@PathVariable Long bookId) {
		service.delete(bookId);
		return ResponseEntity.noContent().build();
	}
	
}
