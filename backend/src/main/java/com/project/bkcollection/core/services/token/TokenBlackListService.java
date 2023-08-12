package com.project.bkcollection.core.services.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bkcollection.core.entities.TokenBlackList;
import com.project.bkcollection.core.repositories.TokenBlackListRepository;
import com.project.bkcollection.core.services.token.exceptions.TokenServiceException;


@Service
public class TokenBlackListService {

    @Autowired
    private TokenBlackListRepository repository;
    
    @Transactional(readOnly = true)
    public void verifyToken(String token) {
        if (repository.existsByToken(token)) {
            throw new TokenServiceException("Invalid Token!");
        }
    }
    
    @Transactional()
    public void addTokenInBlackList(String token) {
        if (!repository.existsByToken(token)) {
        	TokenBlackList tokenBlackList = new TokenBlackList();
            tokenBlackList.setToken(token);
            repository.save(tokenBlackList);
        }
    }

}