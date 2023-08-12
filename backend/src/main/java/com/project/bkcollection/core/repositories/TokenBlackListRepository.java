package com.project.bkcollection.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bkcollection.core.entities.TokenBlackList;

public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Long> {
	
	Boolean existsByToken(String token);
}
