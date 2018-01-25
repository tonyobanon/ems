package com.kylantis.eaa.core.users;

import java.util.Date;

import com.ce.ems.base.classes.Gender;

public class UserProfileSpec {

	private String email;
	private String password;

	private String firstName;
	private String middleName;
	private String lastName;
	private String image;

	private Long phone;
	private Date dateOfBirth;
	private Gender gender;

	private String address;

	private Integer city;
	private String cityName;
	
	private String territory; 
	private String territoryName;
	
	private String country;
	private String countryName;
	private String countryDialingCode;
	
	private String role;

	
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

	public Long getPhone() {
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

	public Integer getCity() {
		return city;
	}

	public UserProfileSpec setCity(Integer city) {
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

	public String getRole() {
		return role;
	}

	public UserProfileSpec setRole(String role) {
		this.role = role;
		return this;
	}

	public String getCityName() {
		return cityName;
	}

	public UserProfileSpec setCityName(String cityName) {
		this.cityName = cityName;
		return this;
	}

	public String getTerritoryName() {
		return territoryName;
	}

	public UserProfileSpec setTerritoryName(String territoryName) {
		this.territoryName = territoryName;
		return this;
	}

	public String getCountryName() {
		return countryName;
	}

	public UserProfileSpec setCountryName(String countryName) {
		this.countryName = countryName;
		return this;
	}

	public String getCountryDialingCode() {
		return countryDialingCode;
	}

	public UserProfileSpec setCountryDialingCode(String countryDialingCode) {
		this.countryDialingCode = countryDialingCode;
		return this;
	}
	
	

}
