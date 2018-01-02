package com.ce.ems.base.classes.spec;

import java.util.Date;

public class CourseResultReportSpec {

	private Long academicSemesterCourseId;
	private Long blobId;
	private Date dateCreated;
	private Long createdBy;

	public Long getAcademicSemesterCourseId() {
		return academicSemesterCourseId;
	}

	public CourseResultReportSpec setAcademicSemesterCourseId(Long academicSemesterCourseId) {
		this.academicSemesterCourseId = academicSemesterCourseId;
		return this;
	}

	public Long getBlobId() {
		return blobId;
	}

	public CourseResultReportSpec setBlobId(Long blobId) {
		this.blobId = blobId;
		return this;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public CourseResultReportSpec setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public CourseResultReportSpec setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
		return this;
	}

}
