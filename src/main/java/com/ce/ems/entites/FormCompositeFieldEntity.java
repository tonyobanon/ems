package com.ce.ems.entites;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.FluentArrayList;
import com.ce.ems.base.classes.FluentHashMap;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Cache
@Entity
public class FormCompositeFieldEntity {
	
	@Id
	String id;
	String name;
	@Index String section;
	String context;
	
	Integer sortOrder;
	Boolean isVisible;
	Boolean isRequired;
	Boolean isDefault;
	
	Date dateCreated;
	
	Boolean allowMultipleChoice;
	Map<String, Object> options;
	String itemsSource;
	List<String> defaultSelections;
	
	public FormCompositeFieldEntity() {
		
		options = new FluentHashMap<>();
		defaultSelections = new FluentArrayList<>();
	}

	public String getId() {
		return id;
	}

	public FormCompositeFieldEntity setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public FormCompositeFieldEntity setName(String name) {
		this.name = name;
		return this;
	}

	public String getSection() {
		return section;
	}

	public FormCompositeFieldEntity setSection(String section) {
		this.section = section;
		return this;
	}
	
	public String getContext() {
		return context;
	}

	public FormCompositeFieldEntity setContext(String context) {
		this.context = context;
		return this;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public FormCompositeFieldEntity setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
		return this;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public FormCompositeFieldEntity setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
		return this;
	}

	public Boolean getIsRequired() {
		return isRequired;
	}

	public FormCompositeFieldEntity setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
		return this;
	}
	
	public Boolean getIsDefault() {
		return isDefault;
	}

	public FormCompositeFieldEntity setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
		return this;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public FormCompositeFieldEntity setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}

	public Boolean getAllowMultipleChoice() {
		return allowMultipleChoice;
	}

	public FormCompositeFieldEntity setAllowMultipleChoice(Boolean allowMultipleChoice) {
		this.allowMultipleChoice = allowMultipleChoice;
		return this;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public FormCompositeFieldEntity setOptions(Map<String, Object> options) {
		this.options = options;
		return this;
	}

	public List<String> getDefaultSelections() {
		return defaultSelections;
	}

	public FormCompositeFieldEntity setDefaultSelections(List<String> defaultSelections) {
		this.defaultSelections = defaultSelections;
		return this;
	}

	public String getItemsSource() {
		return itemsSource;
	}

	public FormCompositeFieldEntity setItemsSource(String itemsSource) {
		this.itemsSource = itemsSource;
		return this;
	}
	
	
}
