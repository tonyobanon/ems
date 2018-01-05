package com.ce.ems.base.classes.spec;

import java.util.Date;

import com.ce.ems.base.classes.Semester;

public class AcademicSemesterSpec {

	private Short lowerYearBound;
	private Short upperYearBound;

	private Semester value;
	
	private Date startDate;
	private Date endDate;
	
	 
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

	public Semester getValue() {
		return value;
	}

	public AcademicSemesterSpec setValue(Semester value) {
		this.value = value;
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
