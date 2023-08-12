package com.project.bkcollection.api.handlers;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bkcollection.api.handlers.models.ExceptionResponse;
import com.project.bkcollection.core.services.token.adapters.TokenService;
import com.project.bkcollection.core.services.token.exceptions.TokenServiceException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenAccessRequestFilter extends OncePerRequestFilter {

    private final static String TOKEN_TYPE = "Bearer ";

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            tryDoFilterInternal(request, response, filterChain);
        } catch (TokenServiceException exception) {
        	
        	HttpStatus status = HttpStatus.UNAUTHORIZED;
        	ExceptionResponse exceptResponse = new ExceptionResponse();
        	exceptResponse.setStatus(status.value());
            exceptResponse.setTimestamp(LocalDateTime.now());
            exceptResponse.setMessage(exception.getLocalizedMessage());
            exceptResponse.setPath(request.getRequestURI());

            String json = objectMapper.writeValueAsString(exceptResponse);
            response.setStatus(status.value());
            response.setHeader("Content-Type", "application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(json);
        }
    }

    private void tryDoFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    	String token = "";
        String email = "";
        String authorizationHeader = request.getHeader("Authorization");

        if (isTokenInHeader(authorizationHeader)) {
            token = authorizationHeader.substring(TOKEN_TYPE.length());
            email = tokenService.getSubjectAccessToken(token);
        }
        if (isEmailNotInContext(email)) {
            addEmailInContext(request, email);
        }

        filterChain.doFilter(request, response);
    }

    private Boolean isTokenInHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith(TOKEN_TYPE);
    }

    private Boolean isEmailNotInContext(String email) {
        return !email.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void addEmailInContext(HttpServletRequest request, String email) {
    	UserDetails userToAdd = userDetailsService.loadUserByUsername(email);

        var authentication = new UsernamePasswordAuthenticationToken(userToAdd, null, userToAdd.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
