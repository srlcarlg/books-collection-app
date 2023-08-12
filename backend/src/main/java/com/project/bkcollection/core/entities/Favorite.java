package com.project.bkcollection.core.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Favorite implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private Long userId;
	
    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;
	
	public Favorite() {
	
	}
	public Favorite(Long id, Long userId, Book book) {
		super();
		this.id = id;
		this.userId = userId;
		this.book = book;
	}


	public Long getId() {
		return id;
	}
	public Long getUserId() {
		return userId;
	}
	public Book getBook() {
		return book;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Favorite other = (Favorite) obj;
		return Objects.equals(id, other.id);
	}
	
}
