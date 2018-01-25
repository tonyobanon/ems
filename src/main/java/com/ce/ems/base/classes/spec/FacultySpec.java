package com.ce.ems.base.classes.spec;

public class FacultySpec {

	private Long id;
	private String name;
	private Long dean;
	private String deanName;

	public String getName() {
		return name;
	}

	public FacultySpec setName(String name) {
		this.name = name;
		return this;
	}

	public Long getId() {
		return id;
	}

	public FacultySpec setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getDean() {
		return dean;
	}

	public FacultySpec setDean(Long dean) {
		this.dean = dean;
		return this;
	}

	public String getDeanName() {
		return deanName;
	}

	public FacultySpec setDeanName(String deanName) {
		this.deanName = deanName;
		return this;
	}

}
