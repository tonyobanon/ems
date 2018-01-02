package com.ce.ems.base.classes.spec;

import java.util.List;

import com.ce.ems.base.classes.Semester;

public class CourseSpec {

	private String code;
	private String name;
	private Short point;
	private Semester semester;
	private List<Long> departmentLevels;
	private List<Long> lecturers;

	
	public String getCode() {
		return code;
	}

	public CourseSpec setCode(String code) {
		this.code = code;
		return this;
	}

	public String getName() {
		return name;
	}

	public CourseSpec setName(String name) {
		this.name = name;
		return this;
	}

	public Short getPoint() {
		return point;
	}

	public CourseSpec setPoint(Short point) {
		this.point = point;
		return this;
	}

	public Semester getSemester() {
		return semester;
	}

	public CourseSpec setSemester(Semester semester) {
		this.semester = semester;
		return this;
	}

	public List<Long> getDepartmentLevels() {
		return departmentLevels;
	}

	public CourseSpec setDepartmentLevels(List<Long> departmentLevels) {
		this.departmentLevels = departmentLevels;
		return this;
	}

	public List<Long> getLecturers() {
		return lecturers;
	}

	public CourseSpec setLecturers(List<Long> lecturers) {
		this.lecturers = lecturers;
		return this;
	}
}
