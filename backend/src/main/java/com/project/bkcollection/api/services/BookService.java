package com.project.bkcollection.api.services;


import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bkcollection.api.dtos.internal.BookDTO;
import com.project.bkcollection.api.dtos.request.BookRequest;
import com.project.bkcollection.api.dtos.request.BookUpdateRequest;
import com.project.bkcollection.api.dtos.response.BookResponse;
import com.project.bkcollection.api.handlers.excepts.CustomNotFoundException;
import com.project.bkcollection.api.handlers.excepts.DatabaseException;
import com.project.bkcollection.api.mappers.AuthorMapper;
import com.project.bkcollection.api.mappers.BookMapper;
import com.project.bkcollection.api.mappers.CategoryMapper;
import com.project.bkcollection.core.entities.Book;
import com.project.bkcollection.core.events.publishers.BookPublisher;
import com.project.bkcollection.core.repositories.BookRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BookService {
	
	@Autowired
	private BookMapper mapper;
	@Autowired
	private BookRepository repository;
	
	@Autowired 
	private AuthorMapper authorMapper;
	@Autowired 
	private CategoryMapper categoryMapper;

    @Autowired
    private BookPublisher bookPublisher;
	
	@Transactional(readOnly = true)
	public Page<BookResponse> findAllPaged(Pageable pageable) {
		Page<Book> listPage = repository.findAll(pageable);
		return listPage.map(x -> mapper.toResponse(x));
	}
	@Transactional(readOnly = true)
	public Page<BookResponse> findAllContainTitlePaged(String title, Pageable pageable) {
		System.out.println("EUUUUUUUUU");
		Page<Book> listPage = repository.findAllByTitleContaining(title, pageable);
		return listPage.map(x -> mapper.toResponse(x));
	}
	
	@Transactional(readOnly = true)
	public BookResponse findById(Long id) {
		return repository.findById(id)
				.map(x -> mapper.toResponse(x))
				.orElseThrow( () -> 
					new CustomNotFoundException()
				);
	}
	
	@Transactional
	public BookResponse insert(BookRequest bookRequest) {
		Book newBook = mapper.toEntity(bookRequest);
		newBook = repository.save(newBook);
		
		if (bookRequest.getBookUrl() != null || bookRequest.getCoverUrl() != null) {
			bookPublisher.publishInsert(bookRequest, newBook.getId());
		}
		
		return mapper.toResponse(newBook);
	}
	
	@Transactional
	public BookResponse update(Long id, BookUpdateRequest bookRequest) {
		try {
			Book entity = repository.getReferenceById(id);
			BookDTO oldEntityDTO = mapper.toDTO(entity);
			
			copyRequestToEntity(bookRequest, entity);
			entity = repository.save(entity);

			bookPublisher.publishUpdate(bookRequest, oldEntityDTO);
			
			return mapper.toResponse(entity);
		} catch (EntityNotFoundException e) {
			throw new CustomNotFoundException();
		}
	}
	
	@Transactional
	public void delete(Long id) {
		try {
			BookDTO entityDTO = mapper.toDTO(repository.getReferenceById(id));
			repository.deleteById(id);
			bookPublisher.publishDelete(entityDTO);
		}
		catch (EmptyResultDataAccessException e) {
			throw new CustomNotFoundException();
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Database Integrity Violation!");
		}
	}
	
	private void copyRequestToEntity(BookUpdateRequest dto, Book entity) {		
		entity.setTitle(
			firstNonNull(dto.getTitle(), entity.getTitle())
		);
		entity.setPublicationYear(
			firstNonNull(dto.getPublicationYear(), entity.getPublicationYear())
		);
		entity.setDescription(
			firstNonNull(dto.getDescription(), entity.getDescription())
		);
		entity.setBookStatus(
			firstNonNull(dto.getBookStatus(), entity.getBookStatus())
		);
		entity.setTitle(
			firstNonNull(dto.getTitle(), entity.getTitle())
		);
		
		if (!dto.getAuthors().isEmpty()) {
			entity.setAuthors(authorMapper.toEntity(dto.getAuthors()));
		}
		if (!dto.getCategories().isEmpty()) {
			entity.setCategories(categoryMapper.toEntity(dto.getCategories()));
		}
	}
}
