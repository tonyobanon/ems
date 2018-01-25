package com.kylantis.eaa.core.forms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.QuestionnaireInputSection;


public class SectionsBuilder {

	private Map<String, String> sectionTitle;
	
	private Map<String, ArrayList<String>> sectionInputs;
	
	private Map<String, String> inputTitles;
	
	private Map<String, String> inputValues;
	
	private SectionsBuilder () {
		
	}
	
	public static SectionsBuilder create(){
		return new SectionsBuilder();
	}
	
	public List<QuestionnaireInputSection> build(){
		
		List<QuestionnaireInputSection> result = new ArrayList<>();	
		
		sectionTitle.forEach((k, v) -> {
			
			QuestionnaireInputSection section = new QuestionnaireInputSection(k, v);
			
			sectionInputs.get(section).forEach((i) -> {
				section.withInput(inputTitles.get(i), inputValues.get(i));
			});
			
			result.add(section);
		});
		
		return result;
	}
	
	public SectionsBuilder withSection(String id, String title){
		sectionTitle.put(id, title);
		sectionInputs.put(id, new ArrayList<>());
		return this;
	}
	
	public SectionsBuilder withInput(String section, String id, String title, String value){
		sectionInputs.get(section).add(id);
		inputTitles.put(id, title);
		inputValues.put(id, value);
		return this;
	}
	
}
