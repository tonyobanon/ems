package com.ce.ems.base.classes;

public enum IndexedNameType {

	APPLICATION(1), USER(2), COURSE(3), ACTIVITY_STREAM(4), ACADEMIC_SEMESTER(5), DEPARTMENT(6);

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
			
		case 4:
			return IndexedNameType.ACTIVITY_STREAM;
			
		case 5:
			return IndexedNameType.ACADEMIC_SEMESTER;
			
		case 6:
			return IndexedNameType.DEPARTMENT;
	
		default:
			throw new IllegalArgumentException("An invalid value was provided");
		}
	}

	public Integer getValue() {
		return value;
	} 
	
}
