package com.ce.ems.base.classes.spec;

import java.util.List;

import com.ce.ems.base.classes.FluentArrayList;

public class ScoreSheet {

	Long academicSemesterCourseId;
	List<TotalSpec> totals;
	List<ResultRecordSheetSpec> resultRecord;
	
	Boolean isFinal;
	
	public ScoreSheet() {
		this.resultRecord = new FluentArrayList<>();
	}

	public Long getAcademicSemesterCourseId() {
		return academicSemesterCourseId;
	}

	public ScoreSheet setAcademicSemesterCourseId(Long academicSemesterCourseId) {
		this.academicSemesterCourseId = academicSemesterCourseId;
		return this;
	}

	public List<TotalSpec> getTotals() {
		return totals;
	}

	public ScoreSheet setTotals(List<TotalSpec> totals) {
		this.totals = totals;
		return this;
	}

	public List<ResultRecordSheetSpec> getResultRecord() {
		return resultRecord;
	}

	public ScoreSheet setResultRecord(List<ResultRecordSheetSpec> resultRecord) {
		this.resultRecord = resultRecord;
		return this;
	}

	public ScoreSheet addRecord(ResultRecordSheetSpec record) {
		this.resultRecord.add(record);
		return this;
	}

	public Boolean getIsFinal() {
		return isFinal;
	}

	public ScoreSheet setIsFinal(Boolean isFinal) {
		this.isFinal = isFinal;
		return this;
	}

}
