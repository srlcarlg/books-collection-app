package com.project.bkcollection.api.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bkcollection.api.dtos.response.BookResponse;
import com.project.bkcollection.api.dtos.response.FavoriteResponse;
import com.project.bkcollection.api.handlers.excepts.CustomNotFoundException;
import com.project.bkcollection.api.mappers.BookMapper;
import com.project.bkcollection.api.mappers.FavoriteMapper;
import com.project.bkcollection.config.SecurityUtils;
import com.project.bkcollection.core.entities.Book;
import com.project.bkcollection.core.entities.Favorite;
import com.project.bkcollection.core.repositories.BookRepository;
import com.project.bkcollection.core.repositories.FavoriteRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FavoriteService {
	
	@Autowired
	private FavoriteRepository repository;

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private FavoriteMapper mapper;
	
	@Autowired BookMapper bookMapper;
	
	@Autowired
	private SecurityUtils securityUtils;
		
	@Transactional(readOnly = true)
	public Page<Object> findAllPaged(Pageable pageable) {
		Long userId = getAuthUserId();
		Page<Favorite> list = repository.findAllPagedByUserId(userId, pageable);
		
		return list.map(x -> mapper.toResponse(x));
	}
	
	@Transactional(readOnly = true)
	public List<BookResponse> filterById(Long id, Boolean isCategory) {
		Long userId = getAuthUserId();
		List<Book> list = isCategory ? repository.findBooksbyCategoryId(userId, id) : repository.findBooksbyAuthorId(userId, id);	
		
		return list.stream().map(x -> bookMapper.toResponse(x)).toList();
	}
	
	@Transactional
	public FavoriteResponse insert(Long bookId) {
		try {
			Book book = bookRepository.getReferenceById(bookId);
			Long userId = getAuthUserId();

			Favorite toSave = new Favorite();
			toSave.setBook(book);
			toSave.setUserId(userId);
			
			return mapper.toResponse(repository.save(toSave));
		} catch (EntityNotFoundException e) {
			throw new CustomNotFoundException();
		}
	}
	
	@Transactional
	public void delete(Long bookId) {
		try {
			Long userId = getAuthUserId();
			Favorite entity = repository.findFavoritebyBookId(userId, bookId);
			repository.delete(entity);
		}
		catch (NoSuchElementException e) {
			throw new CustomNotFoundException();
		}
	}
	
	private Long getAuthUserId() {
		return securityUtils.getAuthenticatedUser().getId();
	}
}
