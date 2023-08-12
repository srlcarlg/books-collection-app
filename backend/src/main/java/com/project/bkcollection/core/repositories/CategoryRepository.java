package com.project.bkcollection.core.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bkcollection.core.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	Optional<Category> findByName(String name);
	
	default Boolean isCategoryAlreadyRegistered(Category category) {
        if (category.getName() == null) {
            return false;
        }
        return findByName(category.getName())
            .map(xCategory -> !xCategory.getId().equals(category.getId()))
            .orElse(false);
	}
	
	default Boolean isCategoryNameRegistred(String name) {
        return findByName(name).isPresent();
	}
}
