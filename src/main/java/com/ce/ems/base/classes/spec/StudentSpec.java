package com.ce.ems.base.classes.spec;

public class StudentSpec {

	private Long id;
	private String name;
	private String matricNumber;
	private Long departmentLevel;
	private String jambRegNo;
	private Double cgpa;
	
	public Long getId() {
		return id;
	}

	public StudentSpec setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public StudentSpec setName(String name) {
		this.name = name;
		return this;
	}

	public String getMatricNumber() {
		return matricNumber;
	}

	public StudentSpec setMatricNumber(String matricNumber) {
		this.matricNumber = matricNumber;
		return this;
	}
	
	public String getJambRegNo() {
		return jambRegNo;
	}

	public StudentSpec setJambRegNo(String jambRegNo) {
		this.jambRegNo = jambRegNo;
		return this;
	}

	public Long getDepartmentLevel() {
		return departmentLevel;
	}

	public StudentSpec setDepartmentLevel(Long departmentLevel) {
		this.departmentLevel = departmentLevel;
		return this;
	}

	public Double getCgpa() {
		return cgpa;
	}

	public StudentSpec setCgpa(Double cgpa) {
		this.cgpa = cgpa;
		return this;
	}
	
}
