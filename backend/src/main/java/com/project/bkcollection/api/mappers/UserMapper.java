package com.project.bkcollection.api.mappers;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.project.bkcollection.api.dtos.request.UserRequest;
import com.project.bkcollection.api.dtos.response.UserResponse;
import com.project.bkcollection.core.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	@Mapping(target = "id", ignore = true)
    User toEntity(UserRequest request);
	Set<User> toEntity(Set<UserRequest> request);

	@Mapping(target = "confirmationPassword", ignore = true)
    UserRequest toDTO(User model);
    
    UserResponse toResponse(User request);
}
