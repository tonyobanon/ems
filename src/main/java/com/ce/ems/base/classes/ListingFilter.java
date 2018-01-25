package com.ce.ems.base.classes;

import java.util.Map;

public class ListingFilter {

	private Map<String, Object> filters;
	
	public ListingFilter(String filterKey, Object filterValue) {
		this.filters = FluentHashMap.forValueMap().with(filterKey, filterValue);
	}
	
	public ListingFilter(Map<String, Object> filters) {
		this.filters = filters;
	}

	public Map<String, Object> getFilters() {
		return filters;
	}

	public ListingFilter setFilters(Map<String, Object> filters) {
		this.filters = filters;
		return this;
	}
}
