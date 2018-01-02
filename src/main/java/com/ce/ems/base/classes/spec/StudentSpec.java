package com.ce.ems.base.classes.spec;

public class StudentSpec {

	private Long id;
	private String matricNumber;
	private Long departmentLevel;
	private String jambRegNo;
	
	public Long getId() {
		return id;
	}

	public StudentSpec setId(Long id) {
		this.id = id;
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
	
}
