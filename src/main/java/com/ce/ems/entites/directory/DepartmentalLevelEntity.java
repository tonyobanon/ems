package com.ce.ems.entites.directory;

import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class DepartmentalLevelEntity {

	@Id
	Long id;
	Long department;
	@Index Integer level;
	List<Long> students;

	public Long getId() {
		return id;
	}

	public DepartmentalLevelEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getDepartment() {
		return department;
	}

	public DepartmentalLevelEntity setDepartment(Long department) {
		this.department = department;
		return this;
	}

	public Integer getLevel() {
		return level;
	}

	public DepartmentalLevelEntity setLevel(Integer level) {
		this.level = level;
		return this;
	}

	public List<Long> getStudents() {
		return students;
	}

	public DepartmentalLevelEntity setStudents(List<Long> students) {
		this.students = students;
		return this;
	}

	public DepartmentalLevelEntity addStudent(Long student) {
		this.students.add(student);
		return this;
	}
	
	public DepartmentalLevelEntity removeStudent(Long student) {
		this.students.remove(student);
		return this;
	}
}
