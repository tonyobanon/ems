package com.ce.ems.entites.directory;

import java.util.List;

import com.ce.ems.base.classes.FluentArrayList;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Cache
@Entity
public class DepartmentEntity {

	@Id Long id;
	String name;
	@Index String faculty;
	List<Long> levels;
	Long headOfDepartment;
	Boolean isAccredited;
	Short duration;
	List<Long> lecturers;

	public DepartmentEntity() {
		this.levels = new FluentArrayList<>();
		this.lecturers = new FluentArrayList<>();
	}
	
	public Long getId() {
		return id;
	}

	public DepartmentEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public DepartmentEntity setName(String name) {
		this.name = name;
		return this;
	}
	
	public List<Long> getLevels() {
		return levels;
	}

	public DepartmentEntity setLevels(List<Long> levels) {
		this.levels = levels;
		return this;
	}

	public Long getFaculty() {
		return Long.parseLong(faculty);
	}

	public DepartmentEntity setFaculty(Long faculty) {
		this.faculty = faculty.toString();
		return this;
	}

	public Long getHeadOfDepartment() {
		return headOfDepartment;
	}

	public DepartmentEntity setHeadOfDepartment(Long headOfDepartment) {
		this.headOfDepartment = headOfDepartment;
		return this;
	}

	public Boolean getIsAccredited() {
		return isAccredited;
	}

	public DepartmentEntity setIsAccredited(Boolean isAccredited) {
		this.isAccredited = isAccredited;
		return this;
	}

	public Short getDuration() {
		return duration;
	}

	public DepartmentEntity setDuration(Short duration) {
		this.duration = duration;
		return this;
	}

	public List<Long> getLecturers() {
		return lecturers;
	}

	public DepartmentEntity setLecturers(List<Long> lecturers) {
		this.lecturers = lecturers;
		return this;
	}
	
	public DepartmentEntity addLecturer(Long lecturer) {
		this.lecturers.add(lecturer);
		return this;
	}
	
	public DepartmentEntity removeLecturer(Long lecturer) {
		this.lecturers.remove(lecturer);
		return this;
	}
}
