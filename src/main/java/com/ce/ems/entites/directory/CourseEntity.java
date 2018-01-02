package com.ce.ems.entites.directory;

import java.util.List;

import com.ce.ems.base.classes.FluentArrayList;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class CourseEntity {

	@Id
	String code;
	String name;
	Short point;
	Integer semester;
	List<Long> departmentLevels;
	List<Long> lecturers;

	public CourseEntity() {
		this.lecturers = new FluentArrayList<>();
	}
	
	public String getCode() {
		return code;
	}

	public CourseEntity setCode(String code) {
		this.code = code;
		return this;
	}

	public String getName() {
		return name;
	}

	public CourseEntity setName(String name) {
		this.name = name;
		return this;
	}

	public Short getPoint() {
		return point;
	}

	public CourseEntity setPoint(Short point) {
		this.point = point;
		return this;
	}

	public Integer getSemester() {
		return semester;
	}

	public CourseEntity setSemester(Integer semester) {
		this.semester = semester;
		return this;
	}

	public List<Long> getDepartmentLevels() {
		return departmentLevels;
	}

	public CourseEntity setDepartmentLevels(List<Long> departmentLevels) {
		this.departmentLevels = departmentLevels;
		return this;
	}

	public List<Long> getLecturers() {
		return lecturers;
	}

	public CourseEntity setLecturers(List<Long> lecturers) {
		this.lecturers = lecturers;
		return this;
	}
	
	public CourseEntity addLecturer(Long lecturer) {
		this.lecturers.add(lecturer);
		return this;
	}
}
