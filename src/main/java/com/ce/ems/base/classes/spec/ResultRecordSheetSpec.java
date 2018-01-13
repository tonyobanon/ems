package com.ce.ems.base.classes.spec;

import java.util.Date;
import java.util.List;

import com.ce.ems.base.classes.ClientResources.ClientRBRef;
import com.ce.ems.base.classes.FluentArrayList;

public class ResultRecordSheetSpec {

	private Long id;
	private Long studentId;
	private String studentMatricNumber;
	private String studentName;
	private String departmentName;
	private ClientRBRef level;
	private List<Integer> scores;
	private Short total;
	private Date lastUpdated;
	private String lastUpdatedBy;
	

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
	
	public String getStudentMatricNumber() {
		return studentMatricNumber;
	}

	public ResultRecordSheetSpec setStudentMatricNumber(String studentMatricNumber) {
		this.studentMatricNumber = studentMatricNumber;
		return this;
	}

	public Long getStudentId() {
		return studentId;
	}

	public ResultRecordSheetSpec setStudentId(Long studentId) {
		this.studentId = studentId;
		return this;
	}

	public String getStudentName() {
		return studentName;
	}

	public ResultRecordSheetSpec setStudentName(String studentName) {
		this.studentName = studentName;
		return this;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public ResultRecordSheetSpec setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
		return this;
	}

	public ClientRBRef getLevel() {
		return level;
	}

	public ResultRecordSheetSpec setLevel(ClientRBRef level) {
		this.level = level;
		return this;
	}

	public List<Integer> getScores() {
		return scores;
	}

	public ResultRecordSheetSpec setScores(List<Integer> scores) {
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

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public ResultRecordSheetSpec setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
		return this;
	}
	
}
