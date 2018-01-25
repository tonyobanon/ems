package com.kylantis.eaa.core.forms;


public abstract class Question {

	protected Object id;

	public Question(Object id) {
		this.id = id;
	}

	public Object getId() {
		return id;
	}
	
	public String hash(){
		return Integer.toString(hashCode());
	}
}
