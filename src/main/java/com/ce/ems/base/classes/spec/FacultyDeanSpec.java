package com.ce.ems.base.classes.spec;

import java.util.Date;

public class FacultyDeanSpec {

	private Long id;
	private Long faculty;
	private Date startDate;
	
	public Long getId() {
		return id;
	}

	public FacultyDeanSpec setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getFaculty() {
		return faculty;
	}

	public FacultyDeanSpec setFaculty(Long faculty) {
		this.faculty = faculty;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public FacultyDeanSpec setStartDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}
}
