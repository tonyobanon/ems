package com.kylantis.eaa.core.users;

import java.util.Date;

import com.ce.ems.base.classes.Gender;

public class UserProfileSpec {

	public String email;
	public String password;

	String firstName;
	String middleName;
	String lastName;
	String image;

	long phone;
	Date dateOfBirth;
	Gender gender;

	public String address;

	public int city;
	public String territory;
	public String country;

	public String getEmail() {
		return email;
	}

	public UserProfileSpec setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public UserProfileSpec setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public UserProfileSpec setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getMiddleName() {
		return middleName;
	}

	public UserProfileSpec setMiddleName(String middleName) {
		this.middleName = middleName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public UserProfileSpec setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}
	
	public String getImage() {
		return image;
	}

	public UserProfileSpec setImage(String image) {
		this.image = image;
		return this;
	}

	public long getPhone() {
		return phone;
	}

	public UserProfileSpec setPhone(long phone) {
		this.phone = phone;
		return this;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public UserProfileSpec setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		return this;
	}

	public Gender getGender() {
		return gender;
	}

	public UserProfileSpec setGender(Gender gender) {
		this.gender = gender;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public UserProfileSpec setAddress(String address) {
		this.address = address;
		return this;
	}

	public int getCity() {
		return city;
	}

	public UserProfileSpec setCity(int city) {
		this.city = city;
		return this;
	}

	public String getTerritory() {
		return territory;
	}

	public UserProfileSpec setTerritory(String territory) {
		this.territory = territory;
		return this;
	}

	public String getCountry() {
		return country;
	}

	public UserProfileSpec setCountry(String country) {
		this.country = country;
		return this;
	}

}
