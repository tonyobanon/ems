package com.ce.ems.base.classes.spec;

import java.util.Date;

import com.ce.ems.base.classes.Semester;

public class AcademicSemesterSpec {

	private Long id;
	
	private Short lowerYearBound;
	private Short upperYearBound;

	private Semester semester;
	private String semesterString;
	
	private Date startDate;
	private Date endDate;
	
	
	public Long getId() {
		return id;
	}

	public AcademicSemesterSpec setId(Long id) {
		this.id = id;
		return this;
	}

	public Short getLowerYearBound() {
		return lowerYearBound; 
	}

	public AcademicSemesterSpec setLowerYearBound(Short lowerYearBound) {
		this.lowerYearBound = lowerYearBound;
		return this;
	}  

	public Short getUpperYearBound() {
		return upperYearBound;
	} 
 
	public AcademicSemesterSpec setUpperYearBound(Short upperYearBound) {
		this.upperYearBound = upperYearBound;
		return this;
	}

	public Semester getSemester() {
		return semester;
	}

	public AcademicSemesterSpec setSemester(Semester semester) {
		this.semester = semester;
		return this;
	}
	
	public String getSemesterString() {
		return semesterString;
	}

	public AcademicSemesterSpec setSemesterString(String semesterString) {
		this.semesterString = semesterString;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public AcademicSemesterSpec setStartDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}

	public Date getEndDate() {
		return endDate;
	}

	public AcademicSemesterSpec setEndDate(Date endDate) {
		this.endDate = endDate;
		return this;
	}
	
}
