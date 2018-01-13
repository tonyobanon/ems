package com.ce.ems.entites.directory;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class DepartmentalLevelStudentsEntity {

	@Id
	Long id;
	List<Long> students;

	public DepartmentalLevelStudentsEntity() {
		this.students = new ArrayList<>();
	}
	
	public Long getId() {
		return id;
	}

	public DepartmentalLevelStudentsEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public List<Long> getStudents() {
		return students;
	}

	public DepartmentalLevelStudentsEntity setStudents(List<Long> students) {
		this.students = students;
		return this;
	}

	public DepartmentalLevelStudentsEntity addStudent(Long student) {
		this.students.add(student);
		return this;
	}
	
	public DepartmentalLevelStudentsEntity removeStudent(Long student) {
		this.students.remove(student);
		return this;
	}
}
