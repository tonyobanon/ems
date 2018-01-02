package com.ce.ems.base.classes;

public class BooleanWrapper {

	private boolean value;

	public BooleanWrapper() {
		this.value = false;
	}
	
	public BooleanWrapper(boolean value) {
		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	public BooleanWrapper setValue(boolean value) {
		this.value = value;
		return this;
	}

}
