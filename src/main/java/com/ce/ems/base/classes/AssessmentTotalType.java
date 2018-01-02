package com.ce.ems.base.classes;

public enum AssessmentTotalType {

	ASSESSMENT(1), EXAM(2);

	private int value;

	private AssessmentTotalType(int value) {
		this.value = value;
	}

	public static AssessmentTotalType from(int value) {

		switch (value) {

		case 1:
			return ASSESSMENT;

		case 2:
			return EXAM;

		default:
			throw new IllegalArgumentException("An invalid value was provided");
		}
	}

	public int getValue() {
		return value;
	}

}
