package com.ce.ems.entites.directory;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class StudentEntity {

	@Id Long id;
	String matricNumber;
	Long departmentLevel;
	String jambRegNo;
	Double cgpa;

	public Long getId() {
		return id;
	}

	public StudentEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public String getMatricNumber() {
		return matricNumber;
	}

	public StudentEntity setMatricNumber(String matricNumber) {
		this.matricNumber = matricNumber;
		return this;
	}

	public Long getDepartmentLevel() {
		return departmentLevel;
	}

	public StudentEntity setDepartmentLevel(Long departmentLevel) {
		this.departmentLevel = departmentLevel;
		return this;
	}

	public String getJambRegNo() {
		return jambRegNo;
	}

	public StudentEntity setJambRegNo(String jambRegNo) {
		this.jambRegNo = jambRegNo;
		return this;
	}

	public Double getCgpa() {
		return cgpa;
	}

	public StudentEntity setCgpa(Double cgpa) {
		this.cgpa = cgpa;
		return this;
	}
}
