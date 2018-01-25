package com.ce.ems.entites.calculation;

import java.util.Date;
import java.util.List;

import com.ce.ems.base.classes.FluentArrayList;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Cache
@Entity
public class AcademicSemesterCourseEntity {

	@Id Long id;
	@Index String academicSemesterId;
	@Index String courseCode;
	Boolean isSheetCreated;
	Date dateSheetCreated;
	Boolean isSheetFinal;
	Date dateSheetFinal;
	List<Long> totals;
	List<Long> students;
	Integer highestScore;
	Date dateUpdated;

	public AcademicSemesterCourseEntity() {
		this.totals = new FluentArrayList<>();
		this.students = new FluentArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public AcademicSemesterCourseEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getAcademicSemesterId() {
		return Long.parseLong(academicSemesterId);
	}

	public AcademicSemesterCourseEntity setAcademicSemesterId(Long academicSemesterId) {
		this.academicSemesterId = academicSemesterId.toString();
		return this;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public AcademicSemesterCourseEntity setCourseCode(String courseCode) {
		this.courseCode = courseCode;
		return this;
	}

	public Boolean getIsSheetCreated() {
		return isSheetCreated;
	}

	public AcademicSemesterCourseEntity setIsSheetCreated(Boolean isSheetCreated) {
		this.isSheetCreated = isSheetCreated;
		return this;
	}

	public Boolean getIsSheetFinal() {
		return isSheetFinal;
	}

	public AcademicSemesterCourseEntity setIsSheetFinal(Boolean isSheetFinal) {
		this.isSheetFinal = isSheetFinal;
		return this;
	}

	public List<Long> getTotals() {
		return totals;
	}

	public AcademicSemesterCourseEntity setTotals(List<Long> totals) {
		this.totals = totals;
		return this;
	}

	public List<Long> getStudents() {
		return students;
	}

	public AcademicSemesterCourseEntity setStudents(List<Long> students) {
		this.students = students;
		return this;
	}
	
	public AcademicSemesterCourseEntity addStudent(Long student) {
		this.students.add(student);
		return this;
	}

	public Date getDateSheetCreated() {
		return dateSheetCreated;
	}

	public AcademicSemesterCourseEntity setDateSheetCreated(Date dateSheetCreated) {
		this.dateSheetCreated = dateSheetCreated;
		return this;
	}

	public Date getDateSheetFinal() {
		return dateSheetFinal;
	}

	public AcademicSemesterCourseEntity setDateSheetFinal(Date dateSheetFinal) {
		this.dateSheetFinal = dateSheetFinal;
		return this;
	}
	
	public Integer getHighestScore() {
		return highestScore;
	}

	public AcademicSemesterCourseEntity setHighestScore(Integer highestScore) {
		this.highestScore = highestScore;
		return this;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public AcademicSemesterCourseEntity setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
		return this;
	}
	
}
