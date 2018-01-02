package com.ce.ems.base.classes.spec;

import java.util.List;

import com.ce.ems.base.classes.FluentArrayList;

public class ScoreSheet {

	Long academicSemesterCourseId;
	List<Integer> totals;
	List<ResultRecordSheetSpec> resultRecord;

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

	public List<Integer> getTotals() {
		return totals;
	}

	public ScoreSheet setTotals(List<Integer> totals) {
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

}
