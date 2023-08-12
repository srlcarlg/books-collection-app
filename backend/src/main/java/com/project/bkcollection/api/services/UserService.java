package com.project.bkcollection.api.services;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bkcollection.api.dtos.request.UserRequest;
import com.project.bkcollection.api.dtos.request.UserUpdateRequest;
import com.project.bkcollection.api.dtos.response.UserResponse;
import com.project.bkcollection.api.dtos.response.UserUpdateResponse;
import com.project.bkcollection.api.handlers.excepts.CustomFieldErrorException;
import com.project.bkcollection.api.handlers.excepts.CustomNotFoundException;
import com.project.bkcollection.api.handlers.excepts.DatabaseException;
import com.project.bkcollection.api.handlers.models.FieldErrorMessage;
import com.project.bkcollection.api.mappers.UserMapper;
import com.project.bkcollection.config.SecurityUtils;
import com.project.bkcollection.core.entities.User;
import com.project.bkcollection.core.entities.enums.TypeUser;
import com.project.bkcollection.core.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private UserMapper mapper;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
    @Autowired
    private SecurityUtils securityUtils;
	
	@Transactional(readOnly = true)
	public Page<UserResponse> findAllPaged(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);
		return list.map(x -> mapper.toResponse(x));
	}
	
	@Transactional(readOnly = true)
	public UserResponse findById(Long id) {
		return repository.findById(id)
				.map(x -> mapper.toResponse(x))
				.orElseThrow( () -> 
					new CustomNotFoundException()
				);
	}
	
	@Transactional
	public UserResponse insert(UserRequest dto) {
		validateInsert(dto);
		
		User newUser = mapper.toEntity(dto);
		newUser.setPassword(
			passwordEncoder.encode(dto.getPassword())
		);
		
		return mapper.toResponse(repository.save(newUser));
	}
	
	@Transactional
	public UserUpdateResponse update(UserUpdateRequest dto) {
		User authenticatedUser = securityUtils.getAuthenticatedUser();
		boolean withPassword = validateAndUpdate(dto, authenticatedUser);
		
		repository.save(authenticatedUser);
		
		return new UserUpdateResponse("User successfully updated!", withPassword);
	}
	
	@Transactional
	public UserResponse updatebyId(Long id, UserRequest dto) {
		try {
			User updateEntity = repository.getReferenceById(id);
			
			updateEntity.setName(
				firstNonNull(dto.getName(), updateEntity.getName())
			);
			updateEntity.setEmail(
				firstNonNull(dto.getEmail(), updateEntity.getEmail())
			);
			updateEntity.setTypeUser(
					firstNonNull(dto.getTypeUser(), updateEntity.getTypeUser())
			);
			updateEntity = repository.save(updateEntity);
			return mapper.toResponse(updateEntity);

		} catch (EntityNotFoundException e) {
			throw new CustomNotFoundException();
		}
	}
	
	@Transactional
	public void delete(Long id) {
		try {
			if (securityUtils.getAuthenticatedUser().getId().equals(id)) {
				throw new DatabaseException("You cannot exclude yourself!");
			}
			repository.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Database Integrity Violation!");
		}
	}

	private void validateInsert(UserRequest dto) {
		
		validateEmail(dto.getEmail());
		validatePassword(dto.getPassword(), dto.getConfirmationPassword());
        
        boolean userIsAdmin = securityUtils.isAdmin();
		if (!userIsAdmin && dto.getTypeUser() != TypeUser.CLIENT) {
        	FieldErrorMessage fieldErrorMessage = new FieldErrorMessage("typeUser", "Only ADMINS can add other ADMINs/LIBRARIANs");
            throw new CustomFieldErrorException(fieldErrorMessage);
		}
	}
	
	private Boolean validateAndUpdate(UserUpdateRequest dto, User authenticatedUser) {
		
		if (dto.getEmail() != null && !authenticatedUser.getEmail().equals(dto.getEmail())) {
			validateEmail(dto.getEmail());
		}
		
		authenticatedUser.setName(
			firstNonNull(dto.getName(), authenticatedUser.getName())
		);
		authenticatedUser.setEmail(
			firstNonNull(dto.getEmail(), authenticatedUser.getEmail())
		);
		
        boolean hasPassword = dto.getPassword() != null 
        			&& dto.getNewPassword() != null 
                	&& dto.getConfirmationPassword() != null;
        if (hasPassword) {
	        String passwordRequest = dto.getPassword();
	        String passwordInDb = authenticatedUser.getPassword();
	        if (!passwordEncoder.matches(passwordRequest, passwordInDb)) {
	        	FieldErrorMessage fieldErrorMessage = new FieldErrorMessage("password", "The entered password is incorrect!");
	            throw new CustomFieldErrorException(fieldErrorMessage);
	        }
	        
	        validatePassword(dto.getNewPassword(), dto.getConfirmationPassword());
	        
	        authenticatedUser.setPassword(
	        	passwordEncoder.encode(dto.getNewPassword())
	        );
	        return true;
        }
        return false;
	}
	
	private void validateEmail(String email) {
		boolean haveEmail = repository.isEmailAlreadyRegistered(email);
		if (haveEmail) {
			throw new DatabaseException("Email already registered!");
		}
	}
	private void validatePassword(String password, String confirmationPassword) {
        if (!password.equals(confirmationPassword)) {
        	FieldErrorMessage fieldErrorMessage = new FieldErrorMessage("confirmationPassword", "Password fields do not match!");
            throw new CustomFieldErrorException(fieldErrorMessage);
        }
	}
}
