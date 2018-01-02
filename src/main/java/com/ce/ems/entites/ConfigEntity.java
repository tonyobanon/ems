package com.ce.ems.entites;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class ConfigEntity {

	@Id
	String key;
	Object value;
	Date dateUpdated;

	public String getKey() {
		return key;
	}

	public ConfigEntity setKey(String key) {
		this.key = key;
		return this;
	}

	public String getValue() {
		return value.toString();
	}

	public ConfigEntity setValue(Object value) {
		this.value = value;
		this.dateUpdated = new Date();
		return this;
	}

}
