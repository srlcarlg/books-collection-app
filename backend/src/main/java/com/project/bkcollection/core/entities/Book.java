package com.project.bkcollection.core.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.project.bkcollection.core.entities.enums.BookStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Book implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    
    private Integer publicationYear;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;
    
    @ManyToMany
    @JoinTable(
        name = "book_author_id",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "book_category_id",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
    
    @OneToMany(mappedBy = "book")
    private List<Favorite> favorites = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bookFile_id", referencedColumnName = "id")
    private FileInfo bookFile;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coverFile_id", referencedColumnName = "id")
    private FileInfo coverFile;
    
    public Book() {
    	
	}
	public Book(Long id, String title, Integer publicationYear, String description, BookStatus bookStatus,
			Set<Author> authors, Set<Category> categories, List<Favorite> favorites, FileInfo bookFile,
			FileInfo coverFile) {
		super();
		this.id = id;
		this.title = title;
		this.publicationYear = publicationYear;
		this.description = description;
		this.bookStatus = bookStatus;
		this.authors = authors;
		this.categories = categories;
		this.favorites = favorites;
		this.bookFile = bookFile;
		this.coverFile = coverFile;
	}

	public Long getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public Integer getPublicationYear() {
		return publicationYear;
	}
	public String getDescription() {
		return description;
	}
	public BookStatus getBookStatus() {
		return bookStatus;
	}
	public Set<Author> getAuthors() {
		return authors;
	}
	public Set<Category> getCategories() {
		return categories;
	}
	public List<Favorite> getFavorites() {
		return favorites;
	}
	public FileInfo getBookFile() {
		return bookFile;
	}
	public FileInfo getCoverFile() {
		return coverFile;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setBookStatus(BookStatus bookStatus) {
		this.bookStatus = bookStatus;
	}
	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}
	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	public void setFavorites(List<Favorite> favorites) {
		this.favorites = favorites;
	}
	public void setBookFile(FileInfo bookFile) {
		this.bookFile = bookFile;
	}
	public void setCoverFile(FileInfo coverFile) {
		this.coverFile = coverFile;
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
		Book other = (Book) obj;
		return Objects.equals(id, other.id);
	}
    	
}
