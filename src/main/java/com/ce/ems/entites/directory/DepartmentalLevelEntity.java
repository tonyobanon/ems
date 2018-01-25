package com.ce.ems.entites.directory;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Cache
@Entity
public class DepartmentalLevelEntity {

	@Id
	Long id;
	Long department;
	@Index Integer level;

	public Long getId() {
		return id;
	}

	public DepartmentalLevelEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getDepartment() {
		return department;
	}

	public DepartmentalLevelEntity setDepartment(Long department) {
		this.department = department;
		return this;
	}

	public Integer getLevel() {
		return level;
	}

	public DepartmentalLevelEntity setLevel(Integer level) {
		this.level = level;
		return this;
	}
}
