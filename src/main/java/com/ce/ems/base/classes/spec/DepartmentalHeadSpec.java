package com.ce.ems.base.classes.spec;

import java.util.Date;

public class DepartmentalHeadSpec {

	private Long id;
	private Long department;
	private Date startDate;

	public Long getId() {
		return id;
	}

	public DepartmentalHeadSpec setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getDepartment() {
		return department;
	}

	public DepartmentalHeadSpec setDepartment(Long department) {
		this.department = department;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public DepartmentalHeadSpec setStartDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}
}
