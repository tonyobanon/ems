package com.ce.ems.models.helpers;

import java.util.HashMap;
import java.util.Map;

import com.ce.ems.base.classes.activitystreams.ActivityEntityKind;
import com.ce.ems.base.classes.activitystreams.CustomPredicate;
import com.ce.ems.base.classes.activitystreams.Preposition;
import com.ce.ems.models.BaseUserModel;
import com.ce.ems.utils.Utils;

public class ActivityBuilder {

	private Long subject;
	private CustomPredicate predicate;
	private ActivityEntityKind kind;
	private Map<Preposition, ActivityEntityKind> prepositions = new HashMap<>();
	
	
	
	
	private static String getHtmlATag(String uri, String name) {
		return uri == null ? name : "<a href='" + uri + "'>" + name + "/>"; 
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		
		
		
		builder
			.append(BaseUserModel.getPersonName(subject))
			.append(" ")
			.append(Utils.prettify(predicate.name()))
			.append(" ")
			.append(kind);
		
		return null;
		
	}
	
}
