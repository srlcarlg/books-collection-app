package com.project.bkcollection.core.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bkcollection.core.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	
	Page<Book> findAllByTitleContaining(String title, Pageable pageable);

	Optional<Book> findByTitle(String title);
	
	default Boolean isBookTitleAlreadyRegistered(String title) {
        return findByTitle(title).isPresent();
	}
}
