package com.ce.ems.entites.directory;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class DepartmentalHeadEntity {

	@Id
	Long id;
	Long department;
	Date startDate;

	public Long getId() {
		return id;
	}

	public DepartmentalHeadEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getDepartment() {
		return department;
	}

	public DepartmentalHeadEntity setDepartment(Long department) {
		this.department = department;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public DepartmentalHeadEntity setStartDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}

}
