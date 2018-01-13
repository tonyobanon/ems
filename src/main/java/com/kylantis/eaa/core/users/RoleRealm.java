package com.kylantis.eaa.core.users;

import java.util.List;

import com.ce.ems.base.classes.FluentArrayList;

public enum RoleRealm {

	ADMIN(1), EXAM_OFFICER(2), HEAD_OF_DEPARTMENT(3), LECTURER(4), DEAN(5), STUDENT(6);

	private Integer value;

	private RoleRealm(Integer value) {
		this.value = value;
	}

	public static RoleRealm from(int value) {

		switch (value) {

		case 1:
			return RoleRealm.ADMIN;

		case 2:
			return RoleRealm.EXAM_OFFICER;

		case 3:
			return RoleRealm.HEAD_OF_DEPARTMENT;

		case 4:
			return RoleRealm.LECTURER;
			
		case 5:
			return RoleRealm.DEAN;
			
		case 6:
			return RoleRealm.STUDENT;
			
		default:
			throw new IllegalArgumentException("An invalid value was provided");
		}
	}

	public Integer getValue() {
		return value;
	}
	
	
	public List<Integer> spec() {
		List<Integer> result = null;
		switch (this) {
		case ADMIN:
			result = adminFunctionalities();
			break;
		case DEAN:
			result = deanFunctionalities();
			break;
		case EXAM_OFFICER:
			result = examOfficerFunctionalities();
			break;
		case HEAD_OF_DEPARTMENT:
			result = hodFunctionalities();
			break;
		case LECTURER:
			result = lecturerFunctionalities();
			break;
		case STUDENT:
			result = studentFunctionalities();
			break;
		} 
		return result; 
	}
	
	public static List<Integer> adminFunctionalities(){
		List<Integer> o = new FluentArrayList<Integer>();
		for(Functionality f : Functionality.values()){
			if(f.getId() > 0) {
				o.add(f.getId());
			}
		}
		return o;
	} 

	private static List<Integer> baseUserFunctionalities() {
		return new FluentArrayList<Integer>()
			.with(Functionality.VIEW_OWN_PROFILE.getId())
			.with(Functionality.MANAGE_OWN_PROFILE.getId())
			.with(Functionality.GET_SEARCHABLE_LISTS.getId())
			.with(Functionality.PERFORM_LIST_OPERATION.getId())
			.with(Functionality.SEARCH_COURSES.getId())
			.with(Functionality.GET_PERSON_NAMES.getId())
			.with(Functionality.GET_REALM_FUNCTIONALITIES.getId())
			.with(Functionality.GET_ROLE_FUNCTIONALITIES.getId())			
			.with(Functionality.VIEW_SCORE_GRADES.getId());
	}
	
	private static List<Integer> baseStaffFunctionalities() {
		return new FluentArrayList<Integer>()
				
			.withAll(baseUserFunctionalities())
			
			.with(Functionality.VIEW_COURSES.getId())
			.with(Functionality.VIEW_ASSESSMENT_TOTALS.getId())
			.with(Functionality.VIEW_SEMESTER_COURSE_RESULT.getId())
			
			.with(Functionality.VIEW_FACULTY_PROFILES.getId())
			.with(Functionality.VIEW_DEPARTMENT_PROFILES.getId())
			.with(Functionality.VIEW_LECTURER_PROFILES.getId())
			.with(Functionality.VIEW_STUDENT_PROFILES.getId())

			.with(Functionality.VIEW_COURSE_SCORE_SHEET.getId());
	}
	
	private static List<Integer> examOfficerFunctionalities() {
		return new FluentArrayList<Integer>()
			
			.withAll(baseStaffFunctionalities())
			
			.with(Functionality.MANAGE_COURSES.getId())
			.with(Functionality.MANAGE_ASSESSMENT_TOTALS.getId())
			.with(Functionality.MANAGE_SCORE_GRADES.getId());
	}
	
	private static List<Integer> hodFunctionalities() {
		return new FluentArrayList<Integer>()
				
				.withAll(examOfficerFunctionalities())
	
				.with(Functionality.MANAGE_LECTURER_PROFILES.getId())
				.with(Functionality.VIEW_STUDENT_SEMESTER_RESULT.getId());
				
	}
	
	private static List<Integer> deanFunctionalities() {
		return new FluentArrayList<Integer>()
				
				.withAll(hodFunctionalities())
				.with(Functionality.MANAGE_DEPARTMENTAL_HEAD_PROFILES.getId());
	}
	
	private static List<Integer> lecturerFunctionalities() {
		return new FluentArrayList<Integer>()
				
				.withAll(baseStaffFunctionalities())				
				.with(Functionality.MANAGE_COURSE_SCORE_SHEET.getId());
	}
	
	private static List<Integer> studentFunctionalities(){
		return new FluentArrayList<Integer>()
				
					.withAll(baseUserFunctionalities())
				
					.with(Functionality.REGISTER_STUDENT_COURSES.getId());
	}
}
