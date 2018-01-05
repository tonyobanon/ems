package com.ce.ems.entites;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ActivityEntity {

	@Id
	Long id;
	String image;
	String text;
	@Index
	Date date;

	public Long getId() {
		return id;
	}

	public ActivityEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public String getImage() {
		return image;
	}

	public ActivityEntity setImage(String image) {
		this.image = image;
		return this;
	}

	public String getText() {
		return text;
	}

	public ActivityEntity setText(String text) {
		this.text = text;
		return this;
	}

	public Date getDate() {
		return date;
	}

	public ActivityEntity setDate(Date date) {
		this.date = date;
		return this;
	}
}
