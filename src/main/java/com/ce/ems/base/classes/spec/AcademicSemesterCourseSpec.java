package com.ce.ems.base.classes.spec;

import java.util.Date;

public class AcademicSemesterCourseSpec {

	private  Long id;
	private  String courseCode;
	private  String courseTitle;
	private Boolean isSheetCreated;
	private Date dateSheetCreated;
	private Boolean isSheetFinal;
	private Date dateSheetFinal;
	private int studentCount;
	private Date dateUpdated;
	
	public AcademicSemesterCourseSpec() {
	}

	public Long getId() {
		return id;
	}

	public AcademicSemesterCourseSpec setId(Long id) {
		this.id = id;
		return this;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public AcademicSemesterCourseSpec setCourseCode(String courseCode) {
		this.courseCode = courseCode;
		return this;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public AcademicSemesterCourseSpec setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
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

	public int getStudentCount() {
		return studentCount;
	}

	public AcademicSemesterCourseSpec setStudentCount(int studentCount) {
		this.studentCount = studentCount;
		return this;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public AcademicSemesterCourseSpec setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
		return this;
	}
}
