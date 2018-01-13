package com.ce.ems.entites;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ActivityStreamEntity {

	@Id
	Long id;
	String image;
	String activity;
	@Index
	Date date;

	public Long getId() {
		return id; 
	}

	public ActivityStreamEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public String getImage() {
		return image;
	}

	public ActivityStreamEntity setImage(String image) {
		this.image = image;
		return this;
	}

	public String getActivity() {
		return activity;
	}

	public ActivityStreamEntity setActivity(String activity) {
		this.activity = activity;
		return this;
	}

	public Date getDate() {
		return date;
	}

	public ActivityStreamEntity setDate(Date date) {
		this.date = date;
		return this;
	}
}
