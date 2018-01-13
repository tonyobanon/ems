package com.ce.ems.base.classes.spec;

public class TotalSpec {

	private Long id;
	private String name;
	private Integer value;

	public Long getId() {
		return id;
	}

	public TotalSpec setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public TotalSpec setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getValue() {
		return value;
	}

	public TotalSpec setValue(Integer value) {
		this.value = value;
		return this;
	}
}
