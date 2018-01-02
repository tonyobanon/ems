package com.ce.ems.base.core;

import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.SearchableUISpec;
import com.ce.ems.base.classes.IndexedNameType;

public abstract class Listable<V> {

	public abstract IndexedNameType type();

	/**
	 * This retrieves a set of objects. It is advised to use a batch get operation
	 * where possible, to reduce costs.
	 */
	public abstract Map<String, V> getAll(List<String> keys);

	/**
	 * This authenticates the user that wants to access this data table
	 */
	public abstract boolean authenticate(Long userId, Map<String, Object> filters);

	public abstract Class<?> entityClass();

	public abstract boolean searchable();

	public SearchableUISpec searchableUiSpec() {
		return null;
	}

}
