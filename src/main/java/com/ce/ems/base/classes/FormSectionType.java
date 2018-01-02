package com.ce.ems.base.classes;

public enum FormSectionType {

	APPLICATION_FORM(1), SYSTEM_CONFIGURATION(2);

	private int value;

	private FormSectionType(int value) {
		this.value = value;
	}

	public static FormSectionType from(int value) {

		switch (value) {

		case 1:
			return APPLICATION_FORM;

		case 2:
			return SYSTEM_CONFIGURATION;

		default:
			throw new IllegalArgumentException("An invalid value was provided");
		}
	}

	public int getValue() {
		return value;
	}

}
