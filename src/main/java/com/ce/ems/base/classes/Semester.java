package com.ce.ems.base.classes;

public enum Semester {

	FIRST(1), SECOND(2);

	private int value;

	private Semester(Integer value) {
		this.value = value;
	}

	public static Semester from(int value) {
   
		switch (value) {

		case 1:
			return Semester.FIRST;
			
		case 2:
			return Semester.SECOND;

		default:
			throw new IllegalArgumentException("An invalid value was provided");
		}
	}

	public int getValue() {
		return value;
	}
	
}
