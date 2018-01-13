package com.ce.ems.base.classes.spec;

import java.util.Date;

public class ActivitySpec {

	private Long id;
	private String image;
	private String html;
	private Date date;

	public Long getId() {
		return id;
	}

	public ActivitySpec setId(Long id) {
		this.id = id;
		return this;
	}

	public String getImage() {
		return image;
	}

	public ActivitySpec setImage(String image) {
		this.image = image;
		return this; 
	}

	public String getHtml() {
		return html;
	}

	public ActivitySpec setHtml(String html) {
		this.html = html;
		return this;
	}

	public Date getDate() {
		return date;
	}

	public ActivitySpec setDate(Date date) {
		this.date = date;
		return this;
	}
}
