package com.ce.ems.base.classes.spec;

public class DepartmentSpec {

	private Long id;
	private String name;
	private Long faculty;
	private String facultyName;
	private Long headOfDepartment;
	private Boolean isAccredited;
	private Short duration;


	public Long getId() {
		return id;
	}

	public DepartmentSpec setId(Long id) {
		this.id = id;
		return this;
	}
	
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
	
	public String getFacultyName() {
		return facultyName;
	}

	public DepartmentSpec setFacultyName(String facultyName) {
		this.facultyName = facultyName;
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
	
	public DepartmentSpec setDuration(int duration) {
		this.duration = (short) duration;
		return this;
	}

}
