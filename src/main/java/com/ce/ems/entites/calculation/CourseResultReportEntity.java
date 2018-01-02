package com.ce.ems.entites.calculation;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class CourseResultReportEntity {

	@Id
	Long academicSemesterCourseId;
	Long blobId;
	Date dateCreated;
	Long createdBy;

	public Long getAcademicSemesterCourseId() {
		return academicSemesterCourseId;
	}

	public CourseResultReportEntity setAcademicSemesterCourseId(Long academicSemesterCourseId) {
		this.academicSemesterCourseId = academicSemesterCourseId;
		return this;
	}

	public Long getBlobId() {
		return blobId;
	}

	public CourseResultReportEntity setBlobId(Long blobId) {
		this.blobId = blobId;
		return this;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public CourseResultReportEntity setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public CourseResultReportEntity setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
		return this;
	}

}
