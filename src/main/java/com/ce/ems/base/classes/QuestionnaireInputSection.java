package com.ce.ems.base.classes;

import java.util.HashMap;
import java.util.Map;

public class QuestionnaireInputSection {

	private String id;
	private String title;
	
	private Map<String, String> inputs;

	public QuestionnaireInputSection(String id, String title, Map<String, String> inputs) {
		this.id = id;
		this.title = title;
		this.inputs = inputs;
	}
	
	public QuestionnaireInputSection(String id, String title) {
		this.id = id;
		this.title = title;
		this.inputs = new HashMap<String, String>();
	}

	public QuestionnaireInputSection() {
		this.inputs = new HashMap<String, String>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public QuestionnaireInputSection withId(String id) {
		this.id = id;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public QuestionnaireInputSection withTitle(String title) {
		this.title = title;
		return this;
	}

	public Map<String, String> getInputs() {
		return inputs;
	}

	public QuestionnaireInputSection setInputs(Map<String, String> inputs) {
		this.inputs = inputs;
		return this;
	}
	
	public QuestionnaireInputSection withInput(String title, String value) {
		this.inputs.put(title, value);
		return this;
	}
	
}
