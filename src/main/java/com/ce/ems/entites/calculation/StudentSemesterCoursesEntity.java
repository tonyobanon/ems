package com.ce.ems.entites.calculation;

import java.util.Date;
import java.util.List;

import com.ce.ems.base.classes.FluentArrayList;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class StudentSemesterCoursesEntity {

	@Id
	Long id;
	@Index
	String studentId;
	@Index
	String academicSemesterId;
	List<String> courses;
	Date dateCreated;

	public StudentSemesterCoursesEntity() {
		courses = new FluentArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public StudentSemesterCoursesEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getStudentId() {
		return Long.parseLong(studentId);
	}

	public StudentSemesterCoursesEntity setStudentId(Long studentId) {
		this.studentId = studentId.toString();
		return this;
	}

	public Long getAcademicSemesterId() {
		return Long.parseLong(academicSemesterId);
	}

	public StudentSemesterCoursesEntity setAcademicSemesterId(Long academicSemesterId) {
		this.academicSemesterId = academicSemesterId.toString();
		return this;
	}

	public List<String> getCourses() {
		return courses;
	}

	public StudentSemesterCoursesEntity setCourses(List<String> courses) {
		this.courses = courses;
		return this;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public StudentSemesterCoursesEntity setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}
}
