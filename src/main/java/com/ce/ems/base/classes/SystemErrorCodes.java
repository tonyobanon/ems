package com.ce.ems.base.classes;

public enum SystemErrorCodes {

	
	// Calculation Module
	
	INVALID_PERCENTILE_ENTERED(99, "Invalid percentile entered"),
	TOTAL_PERCENTILES_SUM_LESS_THAN_100(100, "Total percentile sum of assessment totals are less than 100%"),
	TOTAL_PERCENTILES_SUM_MORE_THAN_100(101, "Total percentile sum of assessment totals are more than 100%"),
	NO_EXAM_TYPED_ASSESSMENT_TOTAL(102, "No exam-typed assessment total was found"),
	MULTIPLE_EXAM_TYPED_ASSESSMENT_TOTAL(103, "Multiple exam-typed assessment total were found"),
	COURSE_RESULT_SHEET_ALREADY_EXISTS(104, "Course result sheet already exists"),
	COURSE_RESULT_SHEET_DOES_NOT_EXIST(105, "Course result sheet does not exist"),
	MAX_CARRY_OVER_UNITS_EXCEEDED(106, "The maximun allowed carryover unit load has been exceeded"),
	RESULT_SHEET_ALREADY_CREATED_FOR_SEMESTER_COURSE(107, "The result sheet has already been created for this course"),
	RESULT_SHEET_NOT_CREATED_FOR_SEMESTER_COURSE(108, "No result sheet has been created for this course"),
	RESULT_SHEET_ALREADY_CREATED_FOR_ASSESSMENT_TOTAL(109, "At least one result sheet has already been created based on the assessment total"),
	STUDENT_SCORE_EXCEEDS_RESULT_SHEET_TOTAL(110, "Student score exceeds result sheet total"),
	INSUFFIECIENT_PERMISSION_FOR_COURSE_RESULT_SHEET(111, "You have insuffiecient permission for creating result sheet for this course"),
	COURSE_RESULT_SHEET_ALREADY_SUBMITTED(130, "Course result sheet has already been submitted"),
	
	
	// Directory Module
	
	DEPARTMENTAL_HEAD_ALREADY_EXISTS(200, "A departmental head already exists"),
	DEPARTMENTAL_HEAD_NOT_FOUND(201, "Departmental head was not found"),
	FACULTY_DEAN_ALREADY_EXISTS(202, "A faculty dean already exists"),
	COURSE_CANNOT_BE_REGISTERED_FOR_THE_CURRENT_SEMESTER(203, "Course cannot be registered for this semster."),
	STUDENT_ALREADY_REGISTERED_COURSES_FOR_THIS_SEMESTER(210, "Your courses have already been registered for this semster"),
	
	
	
	//Roles
	
	DEFAULT_ROLE_CANNOT_BE_DELETED(300, "Role is a default role cannot be deleted"),
	ROLE_IN_USE_AND_CANNOT_BE_DELETED(301, "Role is still in use, and therefore cannot be deleted"),

	
	// Auth
	PASSWORDS_MISMATCH(350, "Password mismatch"),
	EMAIL_ALREADY_EXISTS(351, "Email already exists"),
	PHONE_ALREADY_EXISTS(352, "Phone number already exists"),
	EMAIL_DOES_NOT_EXIST(353, "Email does not exist"),
	PHONE_DOES_NOT_EXIST(354, "Phone does not exist"),
	INCORRECT_PASSWORD(355, "Incorrect password");
	
	private int code;
	private String message;

	private SystemErrorCodes(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public static SystemErrorCodes from(int value) {

		switch (value) {

		case 99:
			return INVALID_PERCENTILE_ENTERED;
			
		case 100:
			return TOTAL_PERCENTILES_SUM_LESS_THAN_100;
			
		case 101:
			return TOTAL_PERCENTILES_SUM_MORE_THAN_100;
			
		case 102:
			return NO_EXAM_TYPED_ASSESSMENT_TOTAL;
			
		case 103:
			return MULTIPLE_EXAM_TYPED_ASSESSMENT_TOTAL;
			
		case 104:
			return COURSE_RESULT_SHEET_ALREADY_EXISTS;
			
		case 105:
			return SystemErrorCodes.COURSE_RESULT_SHEET_DOES_NOT_EXIST;
			
		case 106:
			return SystemErrorCodes.MAX_CARRY_OVER_UNITS_EXCEEDED;
			
		case 107:
			return SystemErrorCodes.RESULT_SHEET_ALREADY_CREATED_FOR_SEMESTER_COURSE;
			
		case 108:
			return SystemErrorCodes.RESULT_SHEET_NOT_CREATED_FOR_SEMESTER_COURSE;
			
		case 109:
			return SystemErrorCodes.RESULT_SHEET_ALREADY_CREATED_FOR_ASSESSMENT_TOTAL;
			
		case 110:
			return SystemErrorCodes.STUDENT_SCORE_EXCEEDS_RESULT_SHEET_TOTAL;
			
		case 111:
			return SystemErrorCodes.INSUFFIECIENT_PERMISSION_FOR_COURSE_RESULT_SHEET;
			
		case 130:
			return SystemErrorCodes.COURSE_RESULT_SHEET_ALREADY_SUBMITTED;
			
			
			
			
		case 200:
			return SystemErrorCodes.DEPARTMENTAL_HEAD_ALREADY_EXISTS;
			
		case 201:
			return SystemErrorCodes.DEPARTMENTAL_HEAD_NOT_FOUND;
			
		case 202:
			return SystemErrorCodes.FACULTY_DEAN_ALREADY_EXISTS;
			
		case 203:
			return SystemErrorCodes.COURSE_CANNOT_BE_REGISTERED_FOR_THE_CURRENT_SEMESTER;
			
		case 210:
			return SystemErrorCodes.STUDENT_ALREADY_REGISTERED_COURSES_FOR_THIS_SEMESTER;
			
			
			
			
			
		case 300:
			return SystemErrorCodes.DEFAULT_ROLE_CANNOT_BE_DELETED;
			
		case 301:
			return SystemErrorCodes.ROLE_IN_USE_AND_CANNOT_BE_DELETED;
			
			
			
		case 350:
			return SystemErrorCodes.PASSWORDS_MISMATCH;
		case 351:
			return SystemErrorCodes.EMAIL_ALREADY_EXISTS;
		case 352:
			return SystemErrorCodes.PHONE_ALREADY_EXISTS;
		case 353:
			return SystemErrorCodes.EMAIL_DOES_NOT_EXIST;
		case 354:
			return SystemErrorCodes.PHONE_DOES_NOT_EXIST;
		case 355:
			return SystemErrorCodes.INCORRECT_PASSWORD;
			
			
		default:
			throw new IllegalArgumentException("An invalid error code was provided");
			
		}
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
}
