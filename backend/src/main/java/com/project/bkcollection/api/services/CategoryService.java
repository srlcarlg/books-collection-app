package com.project.bkcollection.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bkcollection.api.dtos.request.CategoryRequest;
import com.project.bkcollection.api.dtos.response.CategoryResponse;
import com.project.bkcollection.api.handlers.excepts.CustomNotFoundException;
import com.project.bkcollection.api.handlers.excepts.DatabaseException;
import com.project.bkcollection.api.mappers.CategoryMapper;
import com.project.bkcollection.core.entities.Category;
import com.project.bkcollection.core.repositories.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Autowired
	private CategoryMapper mapper;
	
	@Transactional(readOnly = true)
	public Page<CategoryResponse> findAllPaged(Pageable pageable) {
		Page<Category> list = repository.findAll(pageable);
		return list.map(x -> mapper.toResponse(x));
	}
	
	@Transactional(readOnly = true)
	public CategoryResponse findById(Long id) {
		return repository.findById(id)
				.map(x -> mapper.toResponse(x))
				.orElseThrow( () -> 
					new CustomNotFoundException("Category not found!")
				);
	}
	
	@Transactional
	public CategoryResponse insert(CategoryRequest dto) {
		boolean haveName = repository.isCategoryNameRegistred(dto.getName());
		if (haveName) {
			throw new DatabaseException("Category already registered!");
		}
		Category newCategory = mapper.toEntity(dto);
		newCategory.setId(null);
		return mapper.toResponse(repository.save(newCategory));
	}
	
	@Transactional
	public CategoryResponse update(Long id, CategoryRequest dto) {
		try {
			Category updateEntity = repository.getReferenceById(id);
			updateEntity.setName(dto.getName());
			return mapper.toResponse(repository.save(updateEntity));
		} catch (EntityNotFoundException e) {
			throw new CustomNotFoundException();
		}
	}
	
	@Transactional
	public void delete(Long id) {
		try {
			validationDelete(id);
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new CustomNotFoundException();
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Database Integrity Violation!");
		}
	}
	
	@Transactional(readOnly = true)
	private void validationDelete(Long id) {
		Category toExclude;
		try {
			toExclude = repository.getReferenceById(id);
		} catch (EntityNotFoundException e) {
			throw new CustomNotFoundException();
		}
		if (!toExclude.getBooks().isEmpty()) {
			throw new DatabaseException("Cannot delete an Category that contains at least 1 book!");
		}
	}
}
