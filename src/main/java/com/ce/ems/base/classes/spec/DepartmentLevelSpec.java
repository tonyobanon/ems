package com.ce.ems.base.classes.spec;

import com.ce.ems.base.classes.ClientAware;

public class DepartmentLevelSpec {

	Long id;
	Long department;
	Integer level;

	public Long getId() {
		return id;
	}

	public DepartmentLevelSpec setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getDepartment() {
		return department;
	}

	public DepartmentLevelSpec setDepartment(Long department) {
		this.department = department;
		return this;
	}

	public Integer getLevel() {
		return level;
	}

	public DepartmentLevelSpec setLevel(Integer level) {
		this.level = level;
		return this;
	}
	
	@Override
	@ClientAware
	public String toString() {
		return "department_level." + level.toString();
	}
}
