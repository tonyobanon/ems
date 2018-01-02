package com.kylantis.eaa.core.forms;

public class BaseSimpleEntry extends Question {

	private String title;
	private Integer sortOrder;
	private Object context;

	private Boolean isRequired;
	private Boolean isVisible;
	private Boolean isDefault;

	public BaseSimpleEntry(Object id, String title) {
		super(id);
		this.title = title;
		this.sortOrder = 0;
		
		this.isRequired = true;
		this.isVisible = true;
		this.isDefault = false;
	}

	public String getTitle() {
		return title;
	}

	public BaseSimpleEntry setTitle(String title) {
		this.title = title;
		return this;
	}

	public BaseSimpleEntry setId(String id) {
		super.id = id;
		return this;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public BaseSimpleEntry setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
		return this;
	}

	public Object getContext() {
		return context;
	}

	public BaseSimpleEntry setContext(Object context) {
		this.context = context;
		return this;
	}

	public Boolean getIsRequired() {
		return isRequired;
	}

	public BaseSimpleEntry setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
		return this;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public BaseSimpleEntry setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
		return this;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public BaseSimpleEntry setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
		return this;
	}

}
