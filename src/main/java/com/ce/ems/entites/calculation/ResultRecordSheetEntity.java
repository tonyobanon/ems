package com.ce.ems.entites.calculation;

import java.util.Date;
import java.util.List;

import com.ce.ems.base.classes.FluentArrayList;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ResultRecordSheetEntity {

	@Id
	Long id;
	@Index
	String academicSemesterCourseId;
	Long studentId;
	List<Short> scores;
	Short total;
	Date lastUpdated;
	Long lastUpdatedBy;

	public ResultRecordSheetEntity() {
		this.scores = new FluentArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public ResultRecordSheetEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getAcademicSemesterCourseId() {
		return Long.parseLong(academicSemesterCourseId);
	}

	public ResultRecordSheetEntity setAcademicSemesterCourseId(Long academicSemesterCourseId) {
		this.academicSemesterCourseId = academicSemesterCourseId.toString();
		return this;
	}

	public Long getStudentId() {
		return studentId;
	}

	public ResultRecordSheetEntity setStudentId(Long studentId) {
		this.studentId = studentId;
		return this;
	}

	public List<Short> getScores() {
		return scores;
	}

	public ResultRecordSheetEntity setScores(List<Short> scores) {
		this.scores = scores;
		return this;
	}

	public Short getTotal() {
		return total;
	}

	public ResultRecordSheetEntity setTotal(Short total) {
		this.total = total;
		return this;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public ResultRecordSheetEntity setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		return this;
	}

	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public ResultRecordSheetEntity setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
		return this;
	}
	
}
