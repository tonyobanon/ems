package com.ce.ems.base.core;

import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.SearchableUISpec;
import com.ce.ems.base.classes.IndexedNameType;
import com.ce.ems.base.classes.ListingFilter;

public abstract class Listable<S> {

	public abstract IndexedNameType type();

	/**
	 * This retrieves a set of objects. It is advised to use a batch get operation
	 * where possible, to reduce costs.
	 */
	public abstract Map<?, S> getAll(List<String> keys);

	/**
	 * This authenticates the user that wants to access this data table
	 */
	public abstract boolean authenticate(Long userId, List<ListingFilter> listingFilters);

	public abstract Class<?> entityType();

	public abstract boolean searchable();

	public SearchableUISpec searchableUiSpec() {
		return null;
	}

}
