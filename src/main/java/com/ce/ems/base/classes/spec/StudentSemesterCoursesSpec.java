package com.ce.ems.base.classes.spec;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StudentSemesterCoursesSpec {

	private Long id;
	private Long studentId;
	private Long academicSemesterId;
	private Map<String, Short> courses;
	private Date dateCreated;

	public StudentSemesterCoursesSpec() {
		courses = new HashMap<>();
	}

	public Long getId() {
		return id;
	}

	public StudentSemesterCoursesSpec setId(Long id) {
		this.id = id;
		return this;
	}
	
	public Long getStudentId() {
		return studentId;
	}

	public StudentSemesterCoursesSpec setStudentId(Long studentId) {
		this.studentId = studentId;
		return this;
	}

	public Long getAcademicSemesterId() {
		return academicSemesterId;
	}

	public StudentSemesterCoursesSpec setAcademicSemesterId(Long academicSemesterId) {
		this.academicSemesterId = academicSemesterId;
		return this;
	}

	public Map<String, Short> getCourses() {
		return courses;
	}

	public StudentSemesterCoursesSpec setCourses(Map<String, Short> courses) {
		this.courses = courses;
		return this;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public StudentSemesterCoursesSpec setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}

}
