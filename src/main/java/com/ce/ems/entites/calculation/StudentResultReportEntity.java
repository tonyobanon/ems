package com.ce.ems.entites.calculation;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Cache
@Entity
public class StudentResultReportEntity {

	@Id
	Long id;
	@Index
	String studentId;
	@Index
	String academicSemesterId;
	Long blobId;
	Date dateCreated;

	public Long getId() {
		return id;
	}

	public StudentResultReportEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getStudentId() {
		return Long.parseLong(studentId);
	}

	public StudentResultReportEntity setStudentId(Long studentId) {
		this.studentId = studentId.toString();
		return this;
	}

	public Long getAcademicSemesterId() {
		return Long.parseLong(academicSemesterId);
	}

	public StudentResultReportEntity setAcademicSemesterId(Long academicSemesterId) {
		this.academicSemesterId = academicSemesterId.toString();
		return this;
	}

	public Long getBlobId() {
		return blobId;
	}

	public StudentResultReportEntity setBlobId(Long blobId) {
		this.blobId = blobId;
		return this;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public StudentResultReportEntity setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}
}
