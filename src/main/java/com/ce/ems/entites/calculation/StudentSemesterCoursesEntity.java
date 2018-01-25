package com.ce.ems.entites.calculation;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Cache
@Entity
public class StudentSemesterCoursesEntity {

	@Id
	Long id;
	@Index
	String studentId;
	@Index
	String academicSemesterId;
	Map<String, Integer> courses;
	Date dateCreated;

	public StudentSemesterCoursesEntity() {
		courses = new HashMap<>();
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

	public Map<String, Integer> getCourses() {
		return courses;
	}

	public StudentSemesterCoursesEntity setCourses(Map<String, Integer> courses) {
		this.courses = courses;
		return this;
	}
	
	public StudentSemesterCoursesEntity putCourse(String courseCode, Integer score) {
		this.courses.put(courseCode, score);
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
