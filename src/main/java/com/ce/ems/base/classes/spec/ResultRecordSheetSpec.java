package com.ce.ems.base.classes.spec;

import java.util.Date;
import java.util.List;

import com.ce.ems.base.classes.FluentArrayList;

public class ResultRecordSheetSpec {

	private Long id;
	private Long studentId;
	private List<Short> scores;
	private Short total;
	private Date lastUpdated;
	private Long lastUpdatedBy;
	

	public ResultRecordSheetSpec() {
		this.scores = new FluentArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public ResultRecordSheetSpec setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getStudentId() {
		return studentId;
	}

	public ResultRecordSheetSpec setStudentId(Long studentId) {
		this.studentId = studentId;
		return this;
	}

	public List<Short> getScores() {
		return scores;
	}

	public ResultRecordSheetSpec setScores(List<Short> scores) {
		this.scores = scores;
		return this;
	}

	public Short getTotal() {
		return total;
	}

	public ResultRecordSheetSpec setTotal(Short total) {
		this.total = total;
		return this;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public ResultRecordSheetSpec setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		return this;
	}

	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public ResultRecordSheetSpec setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
		return this;
	}
	
}
