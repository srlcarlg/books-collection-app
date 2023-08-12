package com.project.bkcollection.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.project.bkcollection.api.handlers.excepts.CustomNotFoundException;
import com.project.bkcollection.core.entities.User;
import com.project.bkcollection.core.entities.enums.TypeUser;
import com.project.bkcollection.core.repositories.UserRepository;

@Component
public class SecurityUtils {
	
	@Autowired
	private UserRepository userRepository;
    
    public User getAuthenticatedUser() {
        String email = getAuthentication().getName();
        String message = String.format("User with email %s not found!", email);
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomNotFoundException(message));
    }
    
    public Boolean isAdmin() {
        Authentication authentication = getAuthentication();
        String typeUser = TypeUser.ADMIN.name();
        return authentication.getAuthorities()
            .stream()
            .anyMatch(authority -> authority.getAuthority().equals(typeUser));
    }
    public Boolean isClient() {
        Authentication authentication = getAuthentication();
        String typeUser = TypeUser.CLIENT.name();
        return authentication.getAuthorities()
            .stream()
            .anyMatch(authority -> authority.getAuthority().equals(typeUser));
    }
    
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    /*
    public Boolean isLibrarian() {
        Authentication authentication = getAuthentication();
        String typeUser = TypeUser.LIBRARIAN.name();
        return authentication.getAuthorities()
            .stream()
            .anyMatch(authority -> authority.getAuthority().equals(typeUser));
    }
    */
}
