package com.ce.ems.entites.directory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class LevelSemesterEntity {

	@Id
	Long id;
	@Index
	String departmentLevel;
	@Index
	Integer semester;
	List<String> courses;
	Date dateCreated;

	public LevelSemesterEntity() {
		this.courses = new ArrayList<>();
	}	
	
	public Long getId() {
		return id;
	}

	public LevelSemesterEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getDepartmentLevel() {
		return Long.parseLong(departmentLevel);
	}

	public LevelSemesterEntity setDepartmentLevel(Long departmentLevel) {
		this.departmentLevel = departmentLevel.toString();
		return this;
	}

	public Integer getSemester() {
		return semester;
	}

	public LevelSemesterEntity setSemester(Integer semester) {
		this.semester = semester;
		return this;
	}

	public List<String> getCourses() {
		return courses;
	}

	public LevelSemesterEntity setCourses(List<String> courses) {
		this.courses = courses;
		return this;
	}

	public LevelSemesterEntity addCourse(String course) {
		this.courses.add(course);
		return this;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public LevelSemesterEntity setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}

}
