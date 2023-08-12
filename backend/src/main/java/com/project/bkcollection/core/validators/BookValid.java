package com.project.bkcollection.core.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = BookValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BookValid {
	
	String message() default "Validation Error!";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}