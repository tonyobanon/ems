package com.ce.ems.entites;

import java.util.Date;
import java.util.List;

import com.ce.ems.base.classes.FluentArrayList;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfTrue;

@Cache
@Entity
public class UserRoleEntity {

	@Id
	String name;
	List<Integer> spec;
	@Index(IfTrue.class) boolean isDefault;
	@Index Integer realm;
	Date dateCreated;

	public UserRoleEntity() {
		this.spec = new FluentArrayList<>();
	}
	
	public String getName() {
		return name;
	}

	public UserRoleEntity setName(String name) {
		this.name = name;
		return this;
	}

	public List<Integer> getSpec() {
		return spec;
	}

	public UserRoleEntity setSpec(List<Integer> spec) {
		this.spec = spec;
		return this;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public UserRoleEntity setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}

	public Integer getRealm() {
		return realm;
	}

	public UserRoleEntity setRealm(Integer realm) {
		this.realm = realm;
		return this;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public UserRoleEntity setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
		return this;
	}
}
