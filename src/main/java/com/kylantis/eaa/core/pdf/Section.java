package com.kylantis.eaa.core.pdf;

import java.util.List;

import com.kylantis.eaa.core.forms.Question;

public class Section {

	private String id;
	private String title;
	private String summary;
	private List<Question> entries;
	
	public List<Question> getEntries() {
		return entries;
	}

	public Section withEntry(Question entry) {
		this.entries.add(entry);
		return this;
	}

	public Section withEntries(List<Question> entries) {
		this.entries = entries;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Section setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getSummary() {
		return summary;
	}

	public Section setSummary(String summary) {
		this.summary = summary;
		return this;
	}

	public String getId() {
		return id;
	}

	public Section setId(String id) {
		this.id = id;
		return this;
	}
}
