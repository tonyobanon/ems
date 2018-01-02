package com.ce.ems.base.classes.spec;

public class BaseCourseSpec {

	private String code;
	private String name;
	private Short point;

	
	public String getCode() {
		return code;
	}

	public BaseCourseSpec setCode(String code) {
		this.code = code;
		return this;
	}

	public String getName() {
		return name;
	}

	public BaseCourseSpec setName(String name) {
		this.name = name;
		return this;
	}

	public Short getPoint() {
		return point;
	}

	public BaseCourseSpec setPoint(Short point) {
		this.point = point;
		return this;
	}
}
