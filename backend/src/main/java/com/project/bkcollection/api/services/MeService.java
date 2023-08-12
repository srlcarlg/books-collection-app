package com.project.bkcollection.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bkcollection.api.dtos.response.UserResponse;
import com.project.bkcollection.api.mappers.UserMapper;
import com.project.bkcollection.config.SecurityUtils;
import com.project.bkcollection.core.entities.User;

@Service
public class MeService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SecurityUtils securityUtils;

    public UserResponse getAuthenticatedUser() {
        User autheticatedUser = securityUtils.getAuthenticatedUser();
        return userMapper.toResponse(autheticatedUser);
    }

}