package com.ce.ems.base.api.event_streams;

import com.kylantis.eaa.core.users.Functionality;

public enum ObjectType {

	APPLICATION(Article.AN, Functionality.VIEW_APPLICATIONS),

	SYSTEM_CACHE(Article.THE, Functionality.MANAGE_SYSTEM_CACHES),

	SYSTEM_CONFIGURATION(Article.THE, Functionality.VIEW_SYSTEM_CONFIGURATION),

	APPLICATION_FORM(Article.AN, Functionality.MANAGE_APPLICATION_FORMS),

	SYSTEM_CONFIGURATION_FORM(Article.THE, Functionality.MANAGE_SYSTEM_CONFIGURATION_FORM),

	SYSTEM_ROLE(Article.A, Functionality.MANAGE_ROLES),

	COURSE_RESULT_SHEET(Article.THE, Functionality.VIEW_COURSE_RESULT_SHEET),

	SCORE_GRADES(Article.THE, Functionality.VIEW_SCORE_GRADES),
	
	ASSESSMENT_TOTAL(Article.THE, Functionality.VIEW_ASSESSMENT_TOTALS),

	COURSE(Article.A, Functionality.VIEW_COURSES),

	DEPARTMENT(Article.THE, Functionality.VIEW_DEPARTMENT_PROFILES),
	
	FACULTY(Article.THE, Functionality.VIEW_FACULTY_PROFILES),
	
	//For these kinds, Articles must be defined manually by callers
	
	SEMESTER_COURSES(),
	
	EMAIL(),
	
	PHONE_NUMBER(),
	
	PASSWORD(),
	
	IMAGE(),
	
		
	USER_ROLE(Article.THE);
	
	
	private final Article article;
	private final Functionality functionality;

	private ObjectType() {
		this(null);
	}
	
	private ObjectType(Article article) {
		this(article, null);
	}
	
	private ObjectType(Article article, Functionality functionality) {
		this.article = article;
		this.functionality = functionality;
	}
	
	public Article getArticle() {
		return article;
	}
	
	public Functionality getFunctionality() {
		return functionality;
	}
}
