package com.ce.ems.base.classes;

public class IndexedNameSpec {
	
	private String key;
	
	private String x;
	private String y;
	private String z;
	
	public IndexedNameSpec() {
	}
	
	public IndexedNameSpec(String key, String x) {
		this(key, x, null, null);
	}
	
	public IndexedNameSpec(String key, String x, String y) {
		this(key, x, y, null);
	}
	
	public IndexedNameSpec(String key, String x, String y, String z) {
		this.key = key;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String getKey() {
		return key;
	}

	public IndexedNameSpec setKey(String key) {
		this.key = key;
		return this;
	}

	public String getX() {
		return x;
	}

	public IndexedNameSpec setX(String x) {
		this.x = x;
		return this;
	}
	
	public String getY() {
		return y;
	}

	public IndexedNameSpec setY(String y) {
		this.y = y;
		return this;
	}
	

	public String getZ() {
		return z;
	}

	public IndexedNameSpec setZ(String z) {
		this.z = z;
		return this;
	}

}
