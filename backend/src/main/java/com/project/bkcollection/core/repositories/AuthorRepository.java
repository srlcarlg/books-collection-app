package com.project.bkcollection.core.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bkcollection.core.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, Long>{
	
	Optional<Author> findByName(String name);
	
	default Boolean isAuthorAlreadyRegistered(Author author) {
        if (author.getName() == null) {
            return false;
        }
        return findByName(author.getName())
            .map(xAuthor -> !xAuthor.getId().equals(author.getId()))
            .orElse(false);
	}
	
	default Boolean isAuthorNameRegistred(String name) {
        return findByName(name).isPresent();
	}
}
