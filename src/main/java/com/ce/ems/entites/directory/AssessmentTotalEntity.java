package com.ce.ems.entites.directory;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class AssessmentTotalEntity {

	@Id Long id;
	String name;
	Integer type;
	Integer percentile;
	@Index String levelSemester;
	Boolean isValidated;
	Date dateCreated;

	
	public Long getId() {
		return id;
	}

	public AssessmentTotalEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public AssessmentTotalEntity setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getType() {
		return type;
	}

	public AssessmentTotalEntity setType(Integer type) {
		this.type = type;
		return this;
	}

	public Integer getPercentile() {
		return percentile;
	}

	public AssessmentTotalEntity setPercentile(Integer percentile) {
		this.percentile = percentile;
		return this;
	}
	
	public Long getLevelSemester() {
		return Long.parseLong(levelSemester);
	}

	public AssessmentTotalEntity setLevelSemester(Long levelSemester) {
		this.levelSemester = levelSemester.toString();
		return this;
	}

	public Boolean getIsValidated() {
		return isValidated != null ? isValidated : false;
	}

	public AssessmentTotalEntity setIsValidated(Boolean isValidated) {
		this.isValidated = isValidated;
		return this;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public AssessmentTotalEntity setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}
}
