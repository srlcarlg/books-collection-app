package com.project.bkcollection.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.bkcollection.config.auth.models.AuthenticatedUser;
import com.project.bkcollection.core.repositories.UserRepository;

@Service
public class AuthenticationService implements UserDetailsService {
	
	@Autowired 
	private UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		String mensagem = String.format("User with email %s not found!", email);
		return repository.findByEmail(email)
				.map(x -> new AuthenticatedUser(x))
				.orElseThrow(() -> new UsernameNotFoundException(mensagem));
	}

}