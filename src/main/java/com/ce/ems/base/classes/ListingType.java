package com.ce.ems.base.classes;

public enum ListingType {

	LIST(1), SEARCH(2);

	private int value;

	private ListingType(Integer value) {
		this.value = value;
	}

	public static ListingType from(int value) {

		switch (value) {

		case 1:
			return ListingType.LIST;
			
		case 2:
			return ListingType.SEARCH;
			
		default:
			throw new IllegalArgumentException("An invalid value was provided");
		}
	}

	public int getValue() {
		return value;
	}
}
