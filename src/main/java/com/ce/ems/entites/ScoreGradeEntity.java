package com.ce.ems.entites;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Cache
@Entity
public class ScoreGradeEntity {

	@Id
	private String grade;
	private Integer lowerBound;
	private Integer upperBound;

	public String getGrade() {
		return grade;
	}

	public ScoreGradeEntity setGrade(String grade) {
		this.grade = grade;
		return this;
	}

	public Integer getLowerBound() {
		return lowerBound;
	}

	public ScoreGradeEntity setLowerBound(Integer lowerBound) {
		this.lowerBound = lowerBound;
		return this;
	}

	public Integer getUpperBound() {
		return upperBound;
	}

	public ScoreGradeEntity setUpperBound(Integer upperBound) {
		this.upperBound = upperBound;
		return this;
	}
}
