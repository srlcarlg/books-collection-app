package com.project.bkcollection.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

public @interface BooksCollectionPermissions {
	
	@PreAuthorize("hasAnyAuthority('CLIENT', 'LIBRARIAN', 'ADMIN')")
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface isAuthenticated {}
	
	@PreAuthorize("hasAnyAuthority('CLIENT', 'LIBRARIAN')")
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface isClientOrLibrarian {}
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'LIBRARIAN')")
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface isLibrarianOrAdmin {}
	
	@PreAuthorize("hasAuthority('CLIENT')")
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface isClient {}
	
	@PreAuthorize("hasAuthority('LIBRARIAN')")
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface isLibrarian {}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface isAdmin {}
}
