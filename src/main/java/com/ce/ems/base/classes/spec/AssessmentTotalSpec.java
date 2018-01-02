package com.ce.ems.base.classes.spec;

import com.ce.ems.base.classes.AssessmentTotalType;

public class AssessmentTotalSpec {

	private String name;
	private AssessmentTotalType type;
	private Integer percentile;
	private Long levelSemester;
	private Boolean isValidated;
	
	public String getName() {
		return name;
	}

	public AssessmentTotalSpec setName(String name) {
		this.name = name;
		return this;
	}

	public AssessmentTotalType getType() {
		return type;
	}

	public AssessmentTotalSpec setType(AssessmentTotalType type) {
		this.type = type;
		return this;
	}

	public Integer getPercentile() {
		return percentile;
	}

	public AssessmentTotalSpec setPercentile(Integer percentile) {
		this.percentile = percentile;
		return this;
	}

	public Long getLevelSemester() {
		return levelSemester;
	}

	public AssessmentTotalSpec setLevelSemester(Long levelSemester) {
		this.levelSemester = levelSemester;
		return this;
	}

	public Boolean getIsValidated() {
		return isValidated;
	}

	public AssessmentTotalSpec setIsValidated(Boolean isValidated) {
		this.isValidated = isValidated;
		return this;
	}

}
