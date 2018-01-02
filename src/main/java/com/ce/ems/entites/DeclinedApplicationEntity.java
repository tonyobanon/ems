package com.ce.ems.entites;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class DeclinedApplicationEntity {

	@Id Long applicationId;

	Long staffId;
	String reason;

	Date dateCreated;

	public Long getApplicationId() {
		return applicationId;
	}

	public DeclinedApplicationEntity setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
		return this;
	}

	public Long getStaffId() {
		return staffId;
	}

	public DeclinedApplicationEntity setStaffId(Long staffId) {
		this.staffId = staffId;
		return this;
	}

	public String getReason() {
		return reason;
	}

	public DeclinedApplicationEntity setReason(String reason) {
		this.reason = reason;
		return this;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public DeclinedApplicationEntity setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}
	
}
