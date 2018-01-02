package com.ce.ems.base.classes.spec;

import java.util.Date;

public class StudentResultReportSpec {

	private Long id;
	private Long studentId;
	private Long academicSemesterId;
	private Long blobId;
	private Date dateCreated;

	public Long getId() {
		return id;
	}

	public StudentResultReportSpec setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getStudentId() {
		return studentId;
	}

	public StudentResultReportSpec setStudentId(Long studentId) {
		this.studentId = studentId;
		return this;
	}

	public Long getAcademicSemesterId() {
		return academicSemesterId;
	}

	public StudentResultReportSpec setAcademicSemesterId(Long academicSemesterId) {
		this.academicSemesterId = academicSemesterId;
		return this;
	}

	public Long getBlobId() {
		return blobId;
	}

	public StudentResultReportSpec setBlobId(Long blobId) {
		this.blobId = blobId;
		return this;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public StudentResultReportSpec setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}
}
