package com.project.bkcollection.api.handlers;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bkcollection.api.handlers.models.ExceptionResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    	
    	HttpStatus status = HttpStatus.FORBIDDEN;
    	ExceptionResponse exceptResponse = new ExceptionResponse();
        exceptResponse.setStatus(status.value());
        exceptResponse.setTimestamp(LocalDateTime.now());
        exceptResponse.setMessage(accessDeniedException.getLocalizedMessage());
        exceptResponse.setPath(request.getRequestURI());

        String json = objectMapper.writeValueAsString(exceptResponse);
        response.setStatus(status.value());
        response.setHeader("Content-Type", "application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(json);
    }
}
