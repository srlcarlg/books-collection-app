package com.project.bkcollection.core.repositories;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bkcollection.core.entities.Book;
import com.project.bkcollection.core.entities.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	Page<Favorite> findAllPagedByUserId(Long userId, Pageable pageable);
	List<Favorite> findAllByUserId(Long userId);
	
	default List<Book> findBooksbyAuthorId(Long userId,Long authorId) {
		List<Favorite> favorites = findAllByUserId(userId);
		List<Book> books = favorites.stream().map(x -> x.getBook()).collect(Collectors.toList());
		
		List<Book> filtered = books.stream().filter(
				x -> x.getAuthors().stream().anyMatch(y -> y.getId().equals(authorId))
		).collect(Collectors.toList());

		return filtered;
	};
	
	default List<Book> findBooksbyCategoryId(Long userId,Long categoryId) {
		List<Favorite> favorites = findAllByUserId(userId);
		List<Book> books = favorites.stream().map(x -> x.getBook()).collect(Collectors.toList());
		
		List<Book> filtered = books.stream().filter(
				x -> x.getCategories().stream().anyMatch(y -> y.getId().equals(categoryId))
		).collect(Collectors.toList());

		return filtered;
	};
	
	default Favorite findFavoritebyBookId(Long userId, Long bookId) throws NoSuchElementException {
		List<Favorite> favorites = findAllByUserId(userId);
		Favorite toSend = favorites.stream().filter(x -> x.getBook().getId().equals(bookId)).findFirst().get();
		return toSend;
	};
}
