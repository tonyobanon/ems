package com.ce.ems.base.classes.spec;

import java.util.Date;
import java.util.List;

import com.ce.ems.base.classes.FluentArrayList;

public class AcademicSemesterCourseSpec {

	private  Long id;
	private  Long academicSemesterId;
	private  String courseCode;
	private Boolean isSheetCreated;
	private Date dateSheetCreated;
	private Boolean isSheetFinal;
	private Date dateSheetFinal;
	private List<Long> totals;
	private List<Long> students;

	public AcademicSemesterCourseSpec() {
		this.totals = new FluentArrayList<>();
		this.students = new FluentArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public AcademicSemesterCourseSpec setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getAcademicSemesterId() {
		return academicSemesterId;
	}

	public AcademicSemesterCourseSpec setAcademicSemesterId(Long academicSemesterId) {
		this.academicSemesterId = academicSemesterId;
		return this;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public AcademicSemesterCourseSpec setCourseCode(String courseCode) {
		this.courseCode = courseCode;
		return this;
	}

	public Boolean getIsSheetCreated() {
		return isSheetCreated;
	}

	public AcademicSemesterCourseSpec setIsSheetCreated(Boolean isSheetCreated) {
		this.isSheetCreated = isSheetCreated;
		return this;
	}

	public Boolean getIsSheetFinal() {
		return isSheetFinal;
	}

	public AcademicSemesterCourseSpec setIsSheetFinal(Boolean isSheetFinal) {
		this.isSheetFinal = isSheetFinal;
		return this;
	}

	public List<Long> getTotals() {
		return totals;
	}

	public AcademicSemesterCourseSpec setTotals(List<Long> totals) {
		this.totals = totals;
		return this;
	}

	public List<Long> getStudents() {
		return students;
	}

	public AcademicSemesterCourseSpec setStudents(List<Long> students) {
		this.students = students;
		return this;
	}

	public Date getDateSheetCreated() {
		return dateSheetCreated;
	}

	public AcademicSemesterCourseSpec setDateSheetCreated(Date dateSheetCreated) {
		this.dateSheetCreated = dateSheetCreated;
		return this;
	}

	public Date getDateSheetFinal() {
		return dateSheetFinal;
	}

	public AcademicSemesterCourseSpec setDateSheetFinal(Date dateSheetFinal) {
		this.dateSheetFinal = dateSheetFinal;
		return this;
	}
}
