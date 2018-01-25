package com.ce.ems.base.classes.spec;

import java.util.Date;

public class BaseCourseSpec {

	private String code;
	private String name;
	private Short point;
	
	private Long semesterCourseId;

	private Boolean isSheetCreated;
	private Boolean isSheetFinal;
	private Date dateUpdated;
	
	public String getCode() {
		return code;
	}

	public BaseCourseSpec setCode(String code) {
		this.code = code;
		return this;
	}

	public String getName() {
		return name;
	}

	public BaseCourseSpec setName(String name) {
		this.name = name;
		return this;
	}

	public Short getPoint() {
		return point;
	}

	public BaseCourseSpec setPoint(Short point) {
		this.point = point;
		return this;
	}

	public Long getSemesterCourseId() {
		return semesterCourseId;
	}

	public BaseCourseSpec setSemesterCourseId(Long semesterCourseId) {
		this.semesterCourseId = semesterCourseId;
		return this;
	}

	public Boolean getIsSheetCreated() {
		return isSheetCreated;
	}

	public BaseCourseSpec setIsSheetCreated(Boolean isSheetCreated) {
		this.isSheetCreated = isSheetCreated;
		return this;
	}

	public Boolean getIsSheetFinal() {
		return isSheetFinal;
	}

	public BaseCourseSpec setIsSheetFinal(Boolean isSheetFinal) {
		this.isSheetFinal = isSheetFinal;
		return this;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public BaseCourseSpec setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
		return this;
	}
}
