package com.ce.ems.base.classes;

public class IntegerWrapper {

	private int value;

	public IntegerWrapper() {
		value = 0;
	}
	
	public IntegerWrapper(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public IntegerWrapper setValue(int value) {
		this.value = value;
		return this;
	}
	
	public IntegerWrapper add(int value) {
		this.value += value;
		return this;
	}
	
	public IntegerWrapper minus(int value) {
		this.value -= value;
		return this;
	}
}
