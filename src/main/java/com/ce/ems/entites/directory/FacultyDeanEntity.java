package com.ce.ems.entites.directory;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Cache
@Entity
public class FacultyDeanEntity {

	@Id Long id;
	Long faculty;
	Date startDate;
	
	public Long getId() {
		return id;
	}

	public FacultyDeanEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getFaculty() {
		return faculty;
	}

	public FacultyDeanEntity setFaculty(Long faculty) {
		this.faculty = faculty;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public FacultyDeanEntity setStartDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}
}
