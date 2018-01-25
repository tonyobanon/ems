package com.ce.ems.entites.directory;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Cache
@Entity
public class AcademicSemesterEntity {

	@Id Long id;

	Short lowerYearBound;
	Short upperYearBound;

	Integer value;

	Date startDate;
	Date endDate;
	
	public Long getId() {
		return id;
	}

	public AcademicSemesterEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public Short getLowerYearBound() {
		return lowerYearBound;
	}

	public AcademicSemesterEntity setLowerYearBound(Short lowerYearBound) {
		this.lowerYearBound = lowerYearBound;
		return this;
	}

	public Short getUpperYearBound() {
		return upperYearBound;
	}

	public AcademicSemesterEntity setUpperYearBound(Short upperYearBound) {
		this.upperYearBound = upperYearBound;
		return this;
	}

	public Integer getValue() {
		return value;
	}

	public AcademicSemesterEntity setValue(Integer value) {
		this.value = value;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public AcademicSemesterEntity setStartDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}

	public Date getEndDate() {
		return endDate;
	}

	public AcademicSemesterEntity setEndDate(Date endDate) {
		this.endDate = endDate;
		return this;
	}

}
