package com.project.bkcollection.core.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bkcollection.core.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);
		
	default Boolean isEmailAlreadyRegistered(String email) {
        return findByEmail(email).isPresent();
	}
	
}
