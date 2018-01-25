package com.ce.ems.base.classes.spec;

public class ScoreGradeSpec {

	private String grade;
	private Integer lowerBound;
	private Integer upperBound;

	
	
	public ScoreGradeSpec(String grade, Integer lowerBound, Integer upperBound) {
		this.grade = grade;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public String getGrade() {
		return grade;
	}

	public ScoreGradeSpec setGrade(String grade) {
		this.grade = grade;
		return this;
	}

	public Integer getLowerBound() {
		return lowerBound;
	}

	public ScoreGradeSpec setLowerBound(Integer lowerBound) {
		this.lowerBound = lowerBound;
		return this;
	}

	public Integer getUpperBound() {
		return upperBound;
	}

	public ScoreGradeSpec setUpperBound(Integer upperBound) {
		this.upperBound = upperBound;
		return this;
	}
}
