package com.ce.ems.base.classes.spec;

import java.util.List;

import com.ce.ems.base.classes.Semester;

public class LevelSemesterSpec {

	private Long id;
	private Long departmentLevel;
	private Semester semester;
	private List<String> courses;
	
	public Long getId() {
		return id;
	}

	public LevelSemesterSpec setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getDepartmentLevel() {
		return departmentLevel;
	}

	public LevelSemesterSpec setDepartmentLevel(Long departmentLevel) {
		this.departmentLevel = departmentLevel;
		return this;
	}

	public Semester getSemester() {
		return semester;
	}

	public LevelSemesterSpec setSemester(Semester semester) {
		this.semester = semester;
		return this;
	}

	public List<String> getCourses() {
		return courses;
	}

	public LevelSemesterSpec setCourses(List<String> courses) {
		this.courses = courses;
		return this;
	}
}
