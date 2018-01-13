package com.ce.ems.base.api.event_streams;

import com.ce.ems.base.classes.ClientAware;
import com.ce.ems.base.classes.ClientResources.ClientRBRef;

public enum Article {

	A(false), AN(false), THE(true), HIS(false), HER(false);
	
	private final boolean isDefinite;
	
	private Article(boolean isDefinite) {
		this.isDefinite = isDefinite;
	}

	public boolean isDefinite() {
		return isDefinite;
	}
	
	@Override
	@ClientAware
	public String toString() {
		return ClientRBRef.get(this.name().toLowerCase()).toString();
	}
	
}
