package com.kylantis.eaa.core.users;

public enum Functionality {
	
	/* No Auth */
	
	NO_OP(-1, "NO_OP"),
	
	// Forms Module ..
	VIEW_APPLICATION_FORM(-8, "View application form"),
	
	CREATE_APPLICATION(-10, "Create new application"),
	DOWNLOAD_QUESTIONNAIRE(-30, "Download questionairre"),
	
	GET_FORM_FIELD_IDS(-15, "Get form field Ids"),
	UPDATE_APPLICATION(-20, "Update existing application"),
	SUBMIT_APPLICATION(-21, "Sumit application"),
	
	// Directory Module
	LIST_FACULTY_NAMES(-40, "List facultites "),
	LIST_DEPARTMENT_NAMES(-50, "List deoartment names"),
	LIST_DEPARTMENT_LEVELS(-60, "List department levels"),
	LIST_LEVEL_SEMESTERS(-61, "List level semesters"),
	LIST_ACADEMIC_SEMESTER(-70, "List academic semesters"),
	GET_ROLE_REALMS(-80, "List role realms"),
	LIST_ROLES(-81, "List role names"),
	
	GET_CURRENT_SEMESTER(-90, "Get current semester"),
	GET_ALL_SEMESTERS(-95, "Get all semesters"),
	
	SEARCH_COURSES(250, "View courses"),
	
	// Auth/Roles Module
	EMAIL_LOGIN_USER(-100, "Email user Login"),
	PHONE_LOGIN_USER(-110, "Email user Login"),
	
	// Core Module
	GET_LOCATION_DATA(-120, "Get location data"),
	GET_COUNTRY_NAMES(-121, "Get country names"),
	GET_TERRITORY_NAMES(-122, "Get territory names"),
	GET_CITY_NAMES(-123, "Get city names"),
	
	GET_BINARY_DATA(-130, "Get binary data"),
	SAVE_BINARY_DATA(-140, "Save binary data"),
	
	PLATFORM_INSTALLATION(-150, "Install platform"),
	
	GET_AVAILABLE_COUNTRIES(-160, "Get available countries"),
	GET_RESOURCE_BUNDLE_ENTRIES(-170, "Get resource bundle entries"),
	
	
	
	
	/* Basic Auth */
	
	ADD_SYSTEM_MOCK_DATA(1, "Add system mock data", false),
	
	VIEW_OWN_PROFILE(10, "View own profile", false),
	MANAGE_OWN_PROFILE(12, "Manage own profile", false),
	
	GET_SEARCHABLE_LISTS(14, "Get searchable lists", false),
	PERFORM_LIST_OPERATION(15, "Perform list operation", false),
	
	COURSE_SEARCH(20, "Search course", false),

	GET_PERSON_NAMES(80, "Get person names", false),
	
	GET_REALM_FUNCTIONALITIES(101, "Get realm functionalities", false),
	GET_ROLE_FUNCTIONALITIES(102, "Get role functionalities", false),
		
	
	
	
	/* Auth */
	
	
	// Forms Module ..
	MANAGE_APPLICATION_FORMS(30, "Manage application forms"),
	MANAGE_SYSTEM_CONFIGURATION_FORM(109, "Manage system configuration form"),
	
	VIEW_APPLICATIONS(40, "View applications"),
	REVIEW_APPLICATION(50, "Review applications"),
	
	// Core Module ..
	GET_USER_PROFILE(70, "Get user profile"),
	MANAGE_USER_ACCOUNTS(60, "Manage user accounts"),
	
	// Core Module ..
	MANAGE_BINARY_DATA(90, "Manage binary data"),
	
	// Auth/Roles Module
	MANAGE_ROLES(100, "Manage roles"),
	
	// Core Module
	MANAGE_SYSTEM_CACHES(105, "Manage system caches"),
	
	VIEW_SYSTEM_CONFIGURATION(111, "View system configuration"),
	UPDATE_SYSTEM_CONFIGURATION(110, "Update system configuration"),
	
	VIEW_SYSTEM_MTERICS(120, "View system metrics"),
	
	
	// Directory Module
	
	MANAGE_SEMESTER_TIMELINE(130, "Manage semester timeline"), //////////////
	
	MANAGE_DEPARTMENTS(140, "Manage departments"),
	VIEW_DEPARTMENT_PROFILES(150, "View department profiles"),
	
	MANAGE_FACULTIES(160, "Manage facultites"), //////////
	VIEW_FACULTY_PROFILES(170, "View faculty profiles"), //////////
	
	MANAGE_STUDENT_PROFILES(180, "Manage student profiles"),
	VIEW_STUDENT_PROFILES(190, "View student profiles"),
	
	MANAGE_LECTURER_PROFILES(200, "Manage lecturer profiles"),
	VIEW_LECTURER_PROFILES(210, "View lecturer profiles"),
	
	MANAGE_DEPARTMENTAL_HEAD_PROFILES(220, "Manage HOD profiles"), //////////
	MANAGE_FACULTY_DEAN_PROFILES(230, "Manage dean profiles"), //////////
	
	
	MANAGE_COURSES(240, "Manage courses"),
	VIEW_COURSES(250, "View courses"),
	
	REGISTER_STUDENT_COURSES(260, "Register student courses"),
	
	
	
	// Calculation Module
	VIEW_COURSE_RESULT_SHEET(268, "View course result sheet"),
	MANAGE_COURSE_RESULT_SHEET(270, "Manage course result sheet"),
	
	VIEW_ALL_SEMESTER_COURSES(272, "View all semester courses"),
	
	VIEW_ASSESSMENT_TOTALS(280, "View assessment totals"),
	MANAGE_ASSESSMENT_TOTALS(290, "Manage assessment totals"),
	
	
	VIEW_SCORE_GRADES(320, "View score grades"),
	MANAGE_SCORE_GRADES(330, "Manage score grades"),
	  
	MANAGE_ACTIVITY_STREAM(340, "Manage activity Stream"); 
	
	
	
	private final int id;
	private final String name;
	private final Boolean isVisible;

	private Functionality(int id, String name) {
		this(id, name, true);
	}
	
	private Functionality(int id, String name, boolean isVisible) {
		this.id = id;
		this.name = name;
		this.isVisible = isVisible;
	}

	public static Functionality from(Integer value) {

		switch (value) {

		case -8:
			return Functionality.VIEW_APPLICATION_FORM;
		case -10:
			return Functionality.CREATE_APPLICATION;
		case -15:
			return Functionality.GET_FORM_FIELD_IDS;
		case -20:
			return Functionality.UPDATE_APPLICATION;
		case -21:
			return Functionality.SUBMIT_APPLICATION;
		case -30:
			return Functionality.DOWNLOAD_QUESTIONNAIRE;
		case -40:
			return Functionality.LIST_FACULTY_NAMES;
		case -50:
			return Functionality.LIST_DEPARTMENT_NAMES;
		case -60:
			return Functionality.LIST_DEPARTMENT_LEVELS;
		case -61:
			return Functionality.LIST_LEVEL_SEMESTERS;
		case -70:
			return Functionality.LIST_ACADEMIC_SEMESTER;
		case -80:
			return Functionality.GET_ROLE_REALMS;
		case -81:
			return Functionality.LIST_ROLES;
		case -90:
			return Functionality.GET_CURRENT_SEMESTER;
		case -95:
			return Functionality.GET_ALL_SEMESTERS;
		case -100:
			return Functionality.EMAIL_LOGIN_USER;
			
		case -110:
			return Functionality.PHONE_LOGIN_USER;
		case -120:
			return Functionality.GET_LOCATION_DATA;
		case -121:
			return Functionality.GET_COUNTRY_NAMES;
		case -122:
			return Functionality.GET_TERRITORY_NAMES;
		case -123:
			return Functionality.GET_CITY_NAMES;
		case -130:
			return Functionality.GET_BINARY_DATA;
		case -140:
			return Functionality.SAVE_BINARY_DATA;
		case -150:
			return Functionality.PLATFORM_INSTALLATION;
		case -160:
			return Functionality.GET_AVAILABLE_COUNTRIES;
		case -170:
			return Functionality.GET_RESOURCE_BUNDLE_ENTRIES;
			
			
		case 1:
			return Functionality.ADD_SYSTEM_MOCK_DATA;
		case 10:
			return Functionality.VIEW_OWN_PROFILE;
		case 12:
			return Functionality.MANAGE_OWN_PROFILE;
		case 14:
			return Functionality.GET_SEARCHABLE_LISTS;
		case 15:
			return Functionality.PERFORM_LIST_OPERATION;
		case 20:
			return Functionality.COURSE_SEARCH;
		case 30:
			return Functionality.MANAGE_APPLICATION_FORMS;
		case 40:
			return Functionality.VIEW_APPLICATIONS;
		case 50:
			return Functionality.REVIEW_APPLICATION;
		case 60:
			return Functionality.MANAGE_USER_ACCOUNTS;
		case 70:
			return Functionality.GET_USER_PROFILE;
		case 80:
			return Functionality.GET_PERSON_NAMES;
		case 90:
			return Functionality.MANAGE_BINARY_DATA;
		case 100:
			return Functionality.MANAGE_ROLES;
		case 101:
			return Functionality.GET_REALM_FUNCTIONALITIES;
		case 102:
			return Functionality.GET_ROLE_FUNCTIONALITIES;
			
		case 105:
			return Functionality.MANAGE_SYSTEM_CACHES;
		case 109:
			return Functionality.MANAGE_SYSTEM_CONFIGURATION_FORM;
		case 110:
			return Functionality.UPDATE_SYSTEM_CONFIGURATION;
		case 111:
			return Functionality.VIEW_SYSTEM_CONFIGURATION;
		case 120:
			return Functionality.VIEW_SYSTEM_MTERICS;
		case 130:
			return Functionality.MANAGE_SEMESTER_TIMELINE;
		case 140:
			return Functionality.MANAGE_DEPARTMENTS;
		case 150:
			return Functionality.VIEW_DEPARTMENT_PROFILES;
		case 160:
			return Functionality.MANAGE_FACULTIES;
		case 170:
			return Functionality.VIEW_FACULTY_PROFILES;
		case 180:
			return Functionality.MANAGE_STUDENT_PROFILES;
		case 190:
			return Functionality.VIEW_STUDENT_PROFILES;
		case 200:
			return Functionality.MANAGE_LECTURER_PROFILES;
			
		case 210:
			return Functionality.VIEW_LECTURER_PROFILES;
		case 220:
			return Functionality.MANAGE_DEPARTMENTAL_HEAD_PROFILES;
		case 230:
			return Functionality.MANAGE_FACULTY_DEAN_PROFILES;
		case 240:
			return Functionality.MANAGE_COURSES;
		case 250:
			return Functionality.VIEW_COURSES;
		case 260:
			return Functionality.REGISTER_STUDENT_COURSES;			
	
			
		case 268:
			return Functionality.VIEW_COURSE_RESULT_SHEET;
		case 270:
			return Functionality.MANAGE_COURSE_RESULT_SHEET;
		case 272:
			return Functionality.VIEW_ALL_SEMESTER_COURSES;
		case 280:
			return Functionality.VIEW_ASSESSMENT_TOTALS;
		case 290:
			return Functionality.MANAGE_ASSESSMENT_TOTALS;
		case 320:
			return Functionality.VIEW_SCORE_GRADES;
		case 330:
			return Functionality.MANAGE_SCORE_GRADES;
		case 340:
			return Functionality.MANAGE_ACTIVITY_STREAM;
									
		default:
			throw new IllegalArgumentException("An invalid value was provided");
		}
	}

	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}
	
}
