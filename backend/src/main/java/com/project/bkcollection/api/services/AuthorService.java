package com.project.bkcollection.api.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bkcollection.api.dtos.request.AuthorRequest;
import com.project.bkcollection.api.dtos.response.AuthorResponse;
import com.project.bkcollection.api.handlers.excepts.CustomNotFoundException;
import com.project.bkcollection.api.handlers.excepts.DatabaseException;
import com.project.bkcollection.api.mappers.AuthorMapper;
import com.project.bkcollection.core.entities.Author;
import com.project.bkcollection.core.repositories.AuthorRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AuthorService {
	
	@Autowired
	private AuthorRepository repository;
	
	@Autowired
	private AuthorMapper mapper;
	
	@Transactional(readOnly = true)
	public Page<AuthorResponse> findAllPaged(Pageable pageable) {
		Page<Author> list = repository.findAll(pageable);
		return list.map(x -> mapper.toResponse(x));
	}
	
	@Transactional(readOnly = true)
	public AuthorResponse findById(Long id) {
		return repository.findById(id)
				.map(x -> mapper.toResponse(x))
				.orElseThrow( () -> 
					new CustomNotFoundException()
				);
	}
	
	@Transactional
	public AuthorResponse insert(AuthorRequest authorRequest) {
		boolean haveName = repository.isAuthorNameRegistred(authorRequest.getName());
		if (haveName) {
			throw new DatabaseException("Author already registered!");
		}
		Author newAuthor = mapper.toEntity(authorRequest);
		newAuthor.setId(null);
		return mapper.toResponse(repository.save(newAuthor));
	}
	
	@Transactional
	public AuthorResponse update(Long id, AuthorRequest authorRequest) {
		try {
			Author updateEntity = repository.getReferenceById(id);
			updateEntity.setName(authorRequest.getName());
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
	
	private void validationDelete(Long id) {
		Author toExclude;
		try {
			toExclude = repository.getReferenceById(id);
		} catch (EntityNotFoundException e) {
			throw new CustomNotFoundException();
		}
		if (!toExclude.getBooks().isEmpty()) {
			throw new DatabaseException("Cannot delete an Author that contains at least 1 book!");
		}
	}
}
