package com.ce.ems.base.classes;

import java.util.List;

import com.ce.ems.base.classes.activitystreams.ActivityEntityKind;

public class Entity {

	private ActivityEntityKind kind;
	private List<String> identifiers;
	
	
	public ActivityEntityKind getKind() {
		return kind;
	}
	
	public Entity setKind(ActivityEntityKind kind) {
		this.kind = kind;
		return this;
	}
	
	public List<String> getIdentifiers() {
		return identifiers;
	}
	
	public Entity setIdentifiers(List<String> identifiers) {
		this.identifiers = identifiers;
		return this;
	}
}
