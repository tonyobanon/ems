package com.ce.ems.entites;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ApplicationEntity {

	@Id
	Long id;
	//@Index 
	String role;
	@Index Integer status;
	Date dateCreated;
	Date dateUpdated;

	public Long getId() {
		return id;
	}

	public ApplicationEntity setId(Long id) {
		this.id = id;
		return this;
	}
	
	public String getRole() {
		return role;
	}

	public ApplicationEntity setRole(String role) {
		this.role = role;
		return this;
	}

	public Integer getStatus() {
		return status;
	}

	public ApplicationEntity setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public ApplicationEntity setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public ApplicationEntity setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
		return this;
	}
}
