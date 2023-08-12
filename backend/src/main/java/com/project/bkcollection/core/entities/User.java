package com.project.bkcollection.core.entities;

import java.io.Serializable;
import java.util.Objects;

import com.project.bkcollection.core.entities.enums.TypeUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeUser typeUser;
    
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    
    public User() {
		
	}	
	public User(Long id, String name, TypeUser typeUser, String email, String password) {
		super();
		this.id = id;
		this.name = name;
		this.typeUser = typeUser;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public TypeUser getTypeUser() {
		return typeUser;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTypeUser(TypeUser typeUser) {
		this.typeUser = typeUser;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
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
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}   
	
}
