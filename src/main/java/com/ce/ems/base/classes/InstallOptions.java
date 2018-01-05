package com.ce.ems.base.classes;

import java.util.ArrayList;

import com.ce.ems.base.classes.spec.AcademicSemesterSpec;
import com.kylantis.eaa.core.users.UserProfileSpec;

public class InstallOptions {

	//Company Information
	
	public String companyName;
	public String companyLogoUrl;
	
	public String audience;
	public String country;
	
	public Integer studentCount;
	public Integer employeeCount;
	
	public AcademicSemesterSpec academicSemester;
	
	//Administrators
	public ArrayList<UserProfileSpec> admins;

	  
	//Mail Credentials -->
	public MailCredentialSpec mailCredentials;
	 

	//System Settings
	public String currency;
	public String timezone;
	public String language;
	
	
	
	public String getCompanyName() {
		return companyName;
	}
	
	public String getCompanyLogoUrl() {
		return companyLogoUrl;
	}


	public String getAudience() {
		return audience;
	}

	public String getCountry() {
		return country;
	}

	public Integer getStudentCount() {
		return studentCount;
	}
	
	public Integer getEmployeeCount() {
		return employeeCount;
	}
	
	public AcademicSemesterSpec getAcademicSemester() {
		return academicSemester;
	}
	
	
	

	public ArrayList<UserProfileSpec> getAdmins() {
		return admins;
	}
	
	public MailCredentialSpec getMailCredentials() {
		return mailCredentials;
	}

	public String getCurrency() {
		return currency;
	}

	public String getTimezone() {
		return timezone;
	}

	public String getLanguage() {
		return language;
	}
}
