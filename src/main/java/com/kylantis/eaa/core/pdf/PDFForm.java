package com.kylantis.eaa.core.pdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ce.ems.base.core.Todo;
import com.kylantis.eaa.core.forms.InputType;

@Todo("Add inputTypePrefixes helper functions")
public class PDFForm  {
	
	private String logoURL;
	private String title = "";
	
	private String subtitleLeft = "";
	private String subtitleRight = "";
	
	private final Map<InputType, String> inputTypePrefixes;
	private final List<Section> sections;
	
	public PDFForm() {
		this.sections = new ArrayList<>();
		this.inputTypePrefixes = new HashMap<>();
	}

	public List<Section> getSections() {
		return sections;
	}
	
	public PDFForm withSection(Section section) {
	    this.sections.add(section);
	    return this;
	}
	
	public String getLogoURL() {
		return logoURL;
	}

	public PDFForm setLogoURL(String logoURL) {
		this.logoURL = logoURL;
		return this;
	}
	
	public String getTitle() {
		return title;
	}

	public PDFForm setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public String getSubtitleLeft() {
		return subtitleLeft;
	}

	public PDFForm setSubtitleLeft(String subtitleLeft) {
		this.subtitleLeft = subtitleLeft;
		return this;
	}

	public String getSubtitleRight() {
		return subtitleRight;
	}

	public PDFForm setSubtitleRight(String subtitleRight) {
		this.subtitleRight = subtitleRight;
		return this;
	}

	public Map<InputType, String> getInputTypePrefixes() {
		return inputTypePrefixes;
	}
	
	public PDFForm withInputTypePrefix(InputType type, String prefix) {
		this.inputTypePrefixes.put(type, prefix);
		return this;
	}

}
