package com.ce.ems.entites;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotNull;

@Entity
public class FormSectionEntity {

	@Id
	String id;
	@Index
	String name;
	@Index
	Integer type;
	String description;
	@Index(IfNotNull.class)
	Integer realm;

	public String getId() {
		return id;
	}

	public FormSectionEntity setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public FormSectionEntity setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getType() {
		return type;
	}

	public FormSectionEntity setType(Integer type) {
		this.type = type;
		return this;
	}

	public Integer getRealm() {
		return realm;
	}

	public FormSectionEntity setRealm(Integer realm) {
		this.realm = realm;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public FormSectionEntity setDescription(String description) {
		this.description = description;
		return this;
	}
}
