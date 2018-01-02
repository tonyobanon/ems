package com.ce.ems.entites.directory;

import java.util.Date;
import java.util.List;

import com.ce.ems.base.classes.FluentArrayList;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class LecturerEntity {

	@Id
	Long id;
	Long department;
	List<String> courses;
	Date startDate;

	public LecturerEntity() {
		this.courses = new FluentArrayList<>();
	}
	
	public Long getId() {
		return id;
	}

	public LecturerEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getDepartment() {
		return department;
	}

	public LecturerEntity setDepartment(Long department) {
		this.department = department;
		return this;
	}

	public List<String> getCourses() {
		return courses;
	}

	public LecturerEntity setCourses(List<String> courses) {
		this.courses = courses;
		return this;
	}
	
	public LecturerEntity addCourses(List<String> courses) {
		this.courses.addAll(courses);
		return this;
	}
	
	public LecturerEntity removeCourse(String course) {
		this.courses.remove(course);
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public LecturerEntity setStartDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}
	
}
