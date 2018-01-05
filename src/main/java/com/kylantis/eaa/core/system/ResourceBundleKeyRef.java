package com.kylantis.eaa.core.system;

public class ResourceBundleKeyRef {

	private String key;

	public String getKey() {
		return key;
	}

	public ResourceBundleKeyRef setKey(String key) {
		this.key = key;
		return this;
	}
	
	@Override
	public final int hashCode() {
		return key.intern().hashCode();
	}
}
