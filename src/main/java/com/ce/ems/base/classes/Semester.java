package com.ce.ems.base.classes;

public enum Semester {

	FIRST(1), SECOND(2), THIRD(3);

	private Integer value;

	private Semester(Integer value) {
		this.value = value;
	}

	public static Semester from(int value) {
   
		switch (value) {

		case 1:
			return Semester.FIRST;
			
		case 2:
			return Semester.SECOND;
			
		case 3:
			return Semester.THIRD;

		default:
			throw new IllegalArgumentException("An invalid value was provided");
		}
	}

	public Integer getValue() {
		return value;
	}
	
	@Override
	@ClientAware
	public String toString() {
		return  "semester." + value.toString() ;
	}
}
