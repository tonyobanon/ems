package com.ce.ems.base.classes.spec;

import java.util.Date;
import java.util.List;

public class LecturerSpec {

	private Long id;
	private Long department;
	private List<String> courses;
	private Date startDate;

	public Long getId() {
		return id;
	}

	public LecturerSpec setId(Long id) {
		this.id = id;
		return this;
	}
	
	public Long getDepartment() {
		return department;
	}

	public LecturerSpec setDepartment(Long department) {
		this.department = department;
		return this;
	}

	public List<String> getCourses() {
		return courses;
	}

	public LecturerSpec setCourses(List<String> courses) {
		this.courses = courses;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public LecturerSpec setStartDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}
	
}
