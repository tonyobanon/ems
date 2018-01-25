package com.ce.ems.entites;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class BaseUserEntity {

	@Id Long id;

	@Index
	String email;
	String password;

	String firstName;
	String middleName;
	String lastName;
	String image;

	//@Index
	String phone;
	Date dateOfBirth;
	Integer gender;

	String address;

	int city;
	String territory;
	String country;

	@Index String role;

	Long principal;
	
	Date dateCreated;
	Date dateUpdated;

	public Long getId() {
		return id;
	}

	public BaseUserEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public BaseUserEntity setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public BaseUserEntity setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public BaseUserEntity setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getMiddleName() {
		return middleName;
	}

	public BaseUserEntity setMiddleName(String middleName) {
		this.middleName = middleName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public BaseUserEntity setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getImage() {
		return image;
	}

	public BaseUserEntity setImage(String image) {
		this.image = image;
		return this;
	}

	public Long getPhone() {
		return Long.parseLong(phone);
	}

	public BaseUserEntity setPhone(Long phone) {
		this.phone = phone.toString();
		return this;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public BaseUserEntity setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		return this;
	}

	public Integer getGender() {
		return gender;
	}

	public BaseUserEntity setGender(Integer gender) {
		this.gender = gender;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public BaseUserEntity setAddress(String address) {
		this.address = address;
		return this;
	}

	public int getCity() {
		return city;
	}

	public BaseUserEntity setCity(int city) {
		this.city = city;
		return this;
	}

	public String getTerritory() {
		return territory;
	}

	public BaseUserEntity setTerritory(String territory) {
		this.territory = territory;
		return this;
	}

	public String getCountry() {
		return country;
	}

	public BaseUserEntity setCountry(String country) {
		this.country = country;
		return this;
	}

	public String getRole() {
		return role;
	}

	public BaseUserEntity setRole(String role) {
		this.role = role;
		return this;
	}
	
	public Long getPrincipal() {
		return principal;
	}

	public BaseUserEntity setPrincipal(Long principal) {
		this.principal = principal;
		return this;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public BaseUserEntity setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public BaseUserEntity setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
		return this;
	}
	
}
