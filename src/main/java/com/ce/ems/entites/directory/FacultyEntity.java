package com.ce.ems.entites.directory;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class FacultyEntity {

	@Id Long id;
	String name;
	Long dean;

	public Long getId() {
		return id;
	}

	public FacultyEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public FacultyEntity setName(String name) {
		this.name = name;
		return this;
	}

	public Long getDean() {
		return dean;
	}

	public FacultyEntity setDean(Long dean) {
		this.dean = dean;
		return this;
	}
}
