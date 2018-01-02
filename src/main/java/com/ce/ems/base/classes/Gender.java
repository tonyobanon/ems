package com.ce.ems.base.classes;

public enum Gender {

	FEMALE(1), MALE(2);

	private int value;

	private Gender(Integer value) {
		this.value = value;
	}

	public static Gender from(int value) {

		switch (value) {

		case 1:
			return Gender.FEMALE;
			
		case 2:
			return Gender.MALE;
			
		default:
			throw new IllegalArgumentException("An invalid value was provided");
		}
	}

	public int getValue() {
		return value;
	}
}
