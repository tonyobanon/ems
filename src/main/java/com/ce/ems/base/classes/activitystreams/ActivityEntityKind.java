package com.ce.ems.base.classes.activitystreams;

import java.util.List;

import com.ce.ems.base.core.ResourceException;
import com.ce.ems.utils.Utils;
import com.kylantis.eaa.core.fusion.WebRoutes;
import com.kylantis.eaa.core.users.Functionality;

public enum ActivityEntityKind {

	APPLICATION(IndefiniteArticle.AN, Functionality.VIEW_APPLICATIONS),

	USER(IndefiniteArticle.A, Functionality.GET_USER_PROFILE),

	SYSTEM_CACHE(IndefiniteArticle.A, Functionality.MANAGE_SYSTEM_CACHES),

	SYSTEM_CONFIGURATION(IndefiniteArticle.A, Functionality.VIEW_SYSTEM_CONFIGURATION),

	APPLICATION_FORM(IndefiniteArticle.AN, Functionality.MANAGE_APPLICATION_FORMS),

	SYSTEM_CONFIGURATION_FORM(IndefiniteArticle.A, Functionality.MANAGE_SYSTEM_CONFIGURATION_FORM),

	ROLE(IndefiniteArticle.A, Functionality.MANAGE_ROLES),

	COURSE_RESULT(IndefiniteArticle.A, Functionality.VIEW_SEMESTER_COURSE_RESULT),

	COURSE_SCORE_SHEET(IndefiniteArticle.A, Functionality.MANAGE_COURSE_RESULT_SHEET),

	STUDENT_RESULT(IndefiniteArticle.A, Functionality.VIEW_SEMESTER_STUDENT_RESULT),

	ASSESSMENT_TOTAL(IndefiniteArticle.AN, Functionality.VIEW_ASSESSMENT_TOTALS),

	COURSE(IndefiniteArticle.A, Functionality.VIEW_COURSES),

	DEPARTMENT(IndefiniteArticle.A, Functionality.VIEW_DEPARTMENT_PROFILES),

	LECTURER(IndefiniteArticle.A, Functionality.VIEW_LECTURER_PROFILES),

	STUDENT(IndefiniteArticle.A, Functionality.VIEW_STUDENT_PROFILES),;

	private final IndefiniteArticle article;
	private final Functionality functionality;
	
	private List<String> identifiers;


	private ActivityEntityKind(IndefiniteArticle article, Functionality functionality) {
		this.article = article;
		this.functionality = functionality;
	}

	public IndefiniteArticle getArticle() {
		return article;
	}
	
	public Functionality getFunctionality() {
		return functionality;
	}

	public List<String> getIdentifiers() {
		return identifiers;
	}

	public ActivityEntityKind setIdentifiers(List<String> identifiers) {
		this.identifiers = identifiers;
		return this;
	}
	
	private static String getHtmlATag(String uri, String name) {
		return uri == null ? name : "<a href='" + uri + "'>" + name + "/>"; 
	}	

	public static String get(ActivityEntityKind kind, List<String> identifiers) {
	
		Functionality functionality = kind.getFunctionality();
		
		String uri = WebRoutes.getUri(functionality);
		List<String> uriParams = WebRoutes.getUriParams(uri);
		
		if(identifiers.isEmpty()) {
			return getHtmlATag(uri, Utils.prettify(kind.name()));
		}
		
		if(identifiers.size() > uriParams.size()) {
			throw new ResourceException(ResourceException.FAILED_VALIDATION);
		}
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(uri);
		
		for(int i = 0; i < identifiers.size(); i++) {
			
			String k = uriParams.get(i);
			String v = identifiers.get(i);
			
			builder.append("&" + k + "=" + v);
		}
		return getHtmlATag(builder.toString(), Utils.prettify(kind.name()));
	}

}
