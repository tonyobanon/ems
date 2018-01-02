package com.ce.ems.base.classes;

public enum IndexedNameType {

	APPLICATION(1), USER(2), COURSE(3);

	private int value;

	private IndexedNameType(Integer value) {
		this.value = value;
	}

	public static IndexedNameType from(int value) {

		switch (value) {

		case 1:
			return IndexedNameType.APPLICATION;
			
		case 2:
			return IndexedNameType.USER;
			
		case 3:
			return IndexedNameType.COURSE;
	
		default:
			throw new IllegalArgumentException("An invalid value was provided");
		}
	}

	public Integer getValue() {
		return value;
	} 
	
}
