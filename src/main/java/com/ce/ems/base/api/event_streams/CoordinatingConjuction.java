package com.ce.ems.base.api.event_streams;

import com.ce.ems.base.classes.ClientAware;
import com.ce.ems.base.classes.ClientResources.ClientRBRef;

public enum CoordinatingConjuction {
	
	FOR, AND, NOR, BUT, OR, YET, SO;
	
	@Override
	@ClientAware
	public String toString() {
		return ClientRBRef.get(this.name().toLowerCase()).toString();
	}
}
