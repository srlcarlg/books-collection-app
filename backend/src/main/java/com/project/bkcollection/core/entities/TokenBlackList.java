package com.project.bkcollection.core.entities;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TokenBlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

	public TokenBlackList() {
		
	}
	public TokenBlackList(Long id, String token) {
		super();
		this.id = id;
		this.token = token;
	}
	
	public Long getId() {
		return id;
	}
	public String getToken() {
		return token;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setToken(String token) {
		this.token = token;
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
		TokenBlackList other = (TokenBlackList) obj;
		return Objects.equals(id, other.id);
	}
	
}
