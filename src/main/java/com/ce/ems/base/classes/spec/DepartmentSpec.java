package com.ce.ems.base.classes.spec;

public class DepartmentSpec {

	private String name;
	private Long faculty;
	private Long headOfDepartment;
	private Boolean isAccredited;
	private Short duration;

	public String getName() {
		return name;
	}

	public DepartmentSpec setName(String name) {
		this.name = name;
		return this;
	}
	
	public Long getFaculty() {
		return faculty;
	}

	public DepartmentSpec setFaculty(Long faculty) {
		this.faculty = faculty;
		return this;
	}
	
	public Long getHeadOfDepartment() {
		return headOfDepartment;
	}

	public DepartmentSpec setHeadOfDepartment(Long headOfDepartment) {
		this.headOfDepartment = headOfDepartment;
		return this;
	}
	
	public Boolean getIsAccredited() {
		return isAccredited;
	}

	public DepartmentSpec setIsAccredited(Boolean isAccredited) {
		this.isAccredited = isAccredited;
		return this;
	}

	public Short getDuration() {
		return duration;
	}

	public DepartmentSpec setDuration(Short duration) {
		this.duration = duration;
		return this;
	}

}
