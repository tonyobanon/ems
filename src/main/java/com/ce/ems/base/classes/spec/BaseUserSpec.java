package com.ce.ems.base.classes.spec;

import java.util.Date;

import com.ce.ems.base.classes.IndexedNameSpec;

public class BaseUserSpec {
	
	private Long id;
	private String role;
	
	private IndexedNameSpec nameSpec;
	
	private Date dateCreated;
	private Date dateUpdated;

	public Long getId() {
		return id;
	}

	public BaseUserSpec setId(Long id) {
		this.id = id;
		return this;
	}
	
	public String getRole() {
		return role;
	}

	public BaseUserSpec setRole(String role) {
		this.role = role;
		return this;
	}

	public IndexedNameSpec getNameSpec() {
		return nameSpec;
	}

	public BaseUserSpec setNameSpec(IndexedNameSpec nameSpec) {
		this.nameSpec = nameSpec;
		return this;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public BaseUserSpec setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public BaseUserSpec setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
		return this;
	}

}
