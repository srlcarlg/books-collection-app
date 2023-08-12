package com.project.bkcollection.core.validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.project.bkcollection.api.dtos.request.AuthorRequest;
import com.project.bkcollection.api.dtos.request.BookRequest;
import com.project.bkcollection.api.dtos.request.CategoryRequest;
import com.project.bkcollection.api.handlers.models.FieldErrorMessage;
import com.project.bkcollection.core.repositories.AuthorRepository;
import com.project.bkcollection.core.repositories.BookRepository;
import com.project.bkcollection.core.repositories.CategoryRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BookValidator implements ConstraintValidator<BookValid, BookRequest>{

	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private BookRepository bookRepository;
	
	@Override
	public void initialize(BookValid ann) {
	}
	
	@Override
	public boolean isValid(BookRequest value, ConstraintValidatorContext context) {
		List<FieldErrorMessage> errorList = new ArrayList<>();
				
		nameValidation(value, errorList);		
		
		mapValidation(value, errorList);
		
		for (FieldErrorMessage e : errorList) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
			.addConstraintViolation();
		}
		
		return errorList.isEmpty();
	}
	
	@Transactional(readOnly = true)
	private boolean mapValidation(BookRequest dto, List<FieldErrorMessage> errorList) {
		Set<AuthorRequest> authors = dto.getAuthors();
		Set<CategoryRequest> category = dto.getCategories();
		
		if (authors.stream().findFirst().get().getId() == null) {
			errorList.add(new FieldErrorMessage("authors", "Author must be added via ID!")
			);
			return true;
		}
		if (category.stream().findFirst().get().getId() == null) {
			errorList.add(new FieldErrorMessage("categories", "Category must be added via ID!")
			);
			return true;
		}
		Map<Long, Boolean> existsAuthor = new HashMap<>();
		Map<Long, Boolean> existsCategories = new HashMap<>();
		if (!authors.isEmpty()) {
			authors.forEach(x -> 
			existsAuthor.put(x.getId(), authorRepository.existsById(x.getId()))
			);
		}
		if (!category.isEmpty()) {
			category.forEach(x -> 
			existsCategories.put(x.getId(), categoryRepository.existsById(x.getId()))
			);
		}
		
		var noExistsAuthors = existsAuthor.entrySet().stream().filter(x -> x.getValue() == false).collect(Collectors.toList());
		var noExistsCategories = existsCategories.entrySet().stream().filter(x -> x.getValue() == false).collect(Collectors.toList());
		
		if (!noExistsAuthors.isEmpty()) {
			StringBuilder strBuilder = new StringBuilder();
			noExistsAuthors.forEach(x -> 
				strBuilder.append(String.format(" %s,", x.getKey().toString()))
			);
			
			errorList.add(new FieldErrorMessage("authors",
					String.format("There are no Authors with id(s):[%s]", strBuilder.toString()))
			);
		}
		if (!noExistsCategories.isEmpty()) {
			StringBuilder strBuilder = new StringBuilder();
			noExistsCategories.forEach(x -> 
				strBuilder.append(String.format(" %s,", x.getKey().toString()))
			);
			
			errorList.add(new FieldErrorMessage("categories",
					String.format("There are no Categories with id(s):[%s]", strBuilder.toString()))
			);
		}
		return true;
	}
	
	private void nameValidation(BookRequest dto, List<FieldErrorMessage> errorList) {
		if (dto.getTitle().contains("-")) {
			errorList.add(new FieldErrorMessage("title","Title must not contain a Hyphen"));
		}
		if (bookRepository.isBookTitleAlreadyRegistered(dto.getTitle())) {
			errorList.add(new FieldErrorMessage("title","Title already registered!"));
		}
	}
}
