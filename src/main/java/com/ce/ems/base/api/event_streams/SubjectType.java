package com.ce.ems.base.api.event_streams;

import com.kylantis.eaa.core.users.Functionality;

public enum SubjectType {

	USER(Functionality.GET_USER_PROFILE),

	LECTURER(Functionality.VIEW_LECTURER_PROFILES),

	STUDENT(Functionality.VIEW_STUDENT_PROFILES);

	private final Functionality functionality;
	
	private SubjectType(Functionality functionality) {
		this.functionality = functionality;
	}

	public Functionality getFunctionality() {
		return functionality;
	}
}
