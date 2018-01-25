package com.ce.ems.entites;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotEmpty;

@Cache
@Entity
public class IndexedNameEntity {

	@Id
	Long id;
	@Index
	String entityId;
	Integer type;
	@Index
	String x;
	@Index(IfNotEmpty.class)
	String y;
	@Index(IfNotEmpty.class)
	String z;

	public Long getId() {
		return id;
	}

	public IndexedNameEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public String getEntityId() {
		return entityId;
	}

	public IndexedNameEntity setEntityId(String entityId) {
		this.entityId = entityId;
		return this;
	}

	public Integer getType() {
		return type;
	}

	public IndexedNameEntity setType(Integer type) {
		this.type = type;
		return this;
	}

	public String getX() {
		return x;
	}

	public IndexedNameEntity setX(String x) {
		this.x = x;
		return this;
	}

	public String getY() {
		return y;
	}

	public IndexedNameEntity setY(String y) {
		this.y = y;
		return this;
	}
	
	public String getZ() {
		return z;
	}

	public IndexedNameEntity setZ(String z) {
		this.z = z;
		return this;
	}

}
