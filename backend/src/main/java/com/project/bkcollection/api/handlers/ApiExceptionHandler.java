package com.project.bkcollection.api.handlers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.project.bkcollection.api.handlers.excepts.CustomFieldErrorException;
import com.project.bkcollection.api.handlers.excepts.CustomNotFoundException;
import com.project.bkcollection.api.handlers.excepts.DatabaseException;
import com.project.bkcollection.api.handlers.models.ExceptionResponse;
import com.project.bkcollection.api.handlers.models.ValidationResponse;
import com.project.bkcollection.core.services.storage.exceptions.StorageServiceException;
import com.project.bkcollection.core.services.token.exceptions.TokenServiceException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice(annotations = RestController.class)	
public class ApiExceptionHandler {
	
    private SnakeCaseStrategy camelCaseToSnakeCase = new SnakeCaseStrategy();
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> validationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
	    return createValidationResponse(exception, request.getRequestURI());
	}
	
    @ExceptionHandler(CustomFieldErrorException.class)
    public ResponseEntity<Object> customValidationException(CustomFieldErrorException exception) {
        return createCustomFieldErrorResponse(exception);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> validationException(MethodArgumentTypeMismatchException exception, HttpServletRequest request) {
	    return createExceptionResponse(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), request.getRequestURI());
	}
	@ExceptionHandler(CustomNotFoundException.class)
	public ResponseEntity<Object> entityNotFoundException(CustomNotFoundException exception, HttpServletRequest request) {
	    return createExceptionResponse(HttpStatus.NOT_FOUND, exception.getLocalizedMessage(), request.getRequestURI());
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<Object> databaseException(DatabaseException exception, HttpServletRequest request) {
	    return createExceptionResponse(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), request.getRequestURI());
	}
	
    @ExceptionHandler(TokenServiceException.class)
    public ResponseEntity<Object> tokenServiceException(TokenServiceException exception, HttpServletRequest request) {
        return createExceptionResponse(HttpStatus.UNAUTHORIZED, exception.getLocalizedMessage(), request.getRequestURI());
    }
    
    @ExceptionHandler(StorageServiceException.class)
    public ResponseEntity<Object> storageServiceException(StorageServiceException exception, HttpServletRequest request) {
        return createExceptionResponse(HttpStatus.EXPECTATION_FAILED, exception.getLocalizedMessage(), request.getRequestURI());
    }
    
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> IOControllerException(IOException exception, HttpServletRequest request) {
        return createExceptionResponse(HttpStatus.EXPECTATION_FAILED, exception.getLocalizedMessage(), request.getRequestURI());
    }

    private ResponseEntity<Object> createExceptionResponse(HttpStatus status, String message, String path) {
    	
    	ExceptionResponse exceptResponse = new ExceptionResponse();
    	exceptResponse.setStatus(status.value());
    	exceptResponse.setTimestamp(LocalDateTime.now());
    	exceptResponse.setMessage(message);
    	exceptResponse.setPath(path);

        return new ResponseEntity<>(exceptResponse, status);
    }
    
    private ResponseEntity<Object> createValidationResponse(MethodArgumentNotValidException exception, String path) {
    	
    	HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationResponse validResponse = new ValidationResponse();
		validResponse.setTimestamp(LocalDateTime.now());
		validResponse.setStatus(status.value());
		validResponse.setError("Validation Error!");
		validResponse.setMessage("Something wrong there, see below :)");
		validResponse.setPath(path);
		
		for (FieldError f : exception.getBindingResult().getFieldErrors()) {
			String snakeCaseField = camelCaseToSnakeCase.translate(f.getField());
			validResponse.addError(snakeCaseField, f.getDefaultMessage());
		}
		
		return ResponseEntity.status(status).body(validResponse);
    }
    
    private ResponseEntity<Object> createCustomFieldErrorResponse(CustomFieldErrorException exception) {
    	
    	HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
    	Map<String, String> validResponse = new HashMap<>();
        String snakeCaseField = camelCaseToSnakeCase.translate(exception.getFieldName());
        
        validResponse.put(snakeCaseField, exception.getMessage());

        return ResponseEntity.status(status).body(validResponse);
    }
    
}
