package com.ce.ems.models.helpers;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import com.ce.ems.base.classes.Gender;
import com.ce.ems.base.core.Exceptions;
import com.ce.ems.models.BlobStoreModel;
import com.ce.ems.utils.Utils;
import com.kylantis.eaa.core.users.UserProfileSpec;

public class MockHelper {

	public static UserProfileSpec createMockStudent() {
		
		//Create user profile
		
		String image = null;
		try {
			image = BlobStoreModel.save(FileUtils.openInputStream(new File("/tmp/sample_avatars/01.png")));
		} catch (IOException e) {
			Exceptions.throwRuntime(e);
		}
		
		UserProfileSpec userSpec =  new UserProfileSpec()
				.setCountry("NG")
				.setTerritory("NG.05")
				.setCity(2566695)
				.setAddress("1, HKN Avenue, Banana Island")
				.setEmail("donald.duke." + Utils.newShortRandom() + "@ymail.com")
				.setFirstName("Donald")
				.setMiddleName("David")
				.setLastName("Duke")
				.setPassword("passXYZ")
				.setGender(Gender.MALE)
				.setPhone(new Random().nextLong())
				.setImage(image)
				.setDateOfBirth(new Date(1996, 8, 12));
		
		return userSpec;
	}
	
	public static UserProfileSpec createMockLecturer() {
		
		String image = null;
		try {
			image = BlobStoreModel.save(FileUtils.openInputStream(new File("/tmp/sample_avatars/02.png")));
		} catch (IOException e) {
			Exceptions.throwRuntime(e);
		}
		
		UserProfileSpec userSpec = new UserProfileSpec()
				.setCountry("NG")
				.setTerritory("NG.05")
				.setCity(2566680)
				.setAddress("1, Diva Street, Chelsea Ave.")
				.setEmail("henry.chapman." + Utils.newShortRandom() + "@ymail.com")
				.setFirstName("Henry")
				.setMiddleName("Tony")
				.setLastName("Chapman")
				.setPassword("passXYZ")
				.setGender(Gender.MALE)
				.setPhone(new Random().nextLong())
				.setImage(image)
				.setDateOfBirth(new Date(1950, 11, 30));
		
		return userSpec;
	}
	
	public static UserProfileSpec createMockHod() {
		
		String image = null;
		try {
			image = BlobStoreModel.save(FileUtils.openInputStream(new File("/tmp/sample_avatars/03.png")));
		} catch (IOException e) {
			Exceptions.throwRuntime(e);
		}
		
		UserProfileSpec userSpec = new  UserProfileSpec()
				.setCountry("NG")
				.setTerritory("NG.05")
				.setCity(2566636)
				.setAddress("26, Rails way Avenue, Indigo Estate")
				.setEmail("vivian.fowler." + Utils.newShortRandom() + "@ymail.com")
				.setFirstName("Vivian")
				.setMiddleName("Rita")
				.setLastName("Fowler")
				.setPassword("passXYZ")
				.setGender(Gender.FEMALE)
				.setPhone(new Random().nextLong())
				.setImage(image)
				.setDateOfBirth(new Date(1972, 8, 3));
		
		return userSpec;
	}
	
	public static UserProfileSpec createMockDean() {
		
		String image = null;
		try {
			image = BlobStoreModel.save(FileUtils.openInputStream(new File("/tmp/sample_avatars/04.png")));
		} catch (IOException e) {
			Exceptions.throwRuntime(e);
		}
		
		UserProfileSpec userSpec = new  UserProfileSpec()
				.setCountry("NG")
				.setTerritory("NG.05")
				.setCity(2351943)
				.setAddress("1, ART Avenue, Dukeson Estate")
				.setEmail("helen.keller." + Utils.newShortRandom() + "@gmail.com")
				.setFirstName("Helen")
				.setMiddleName("Mary")
				.setLastName("Keller")
				.setPassword("passXYZ")
				.setGender(Gender.FEMALE)
				.setPhone(new Random().nextLong())
				.setImage(image)
				.setDateOfBirth(new Date(1990, 04, 19));
		
		return userSpec;
	}

	public static UserProfileSpec createMockAdmin() {
		
		String image = null;
		try {
			image = BlobStoreModel.save(FileUtils.openInputStream(new File("/tmp/sample_avatars/05.png")));
		} catch (IOException e) {
			Exceptions.throwRuntime(e);
		}
		
		UserProfileSpec userSpec = new  UserProfileSpec()
				.setCountry("NG")
				.setTerritory("NG.05")
				.setCity(2566636)
				.setAddress("16, Parkview Estate")
				.setEmail("juliet.rita." + Utils.newShortRandom() + "@ymail.com")
				.setFirstName("Juliet")
				.setMiddleName("Rita")
				.setLastName("Caine")
				.setPassword("passXYZ")
				.setGender(Gender.FEMALE)
				.setPhone(new Random().nextLong())
				.setImage(image)
				.setDateOfBirth(new Date(1989, 8, 3));
		
		return userSpec;
	}
	
	public static UserProfileSpec createMockExamOfficer() {
		
		String image = null;
		try {
			image = BlobStoreModel.save(FileUtils.openInputStream(new File("/tmp/sample_avatars/06.png")));
		} catch (IOException e) {
			Exceptions.throwRuntime(e);
		}
		
		UserProfileSpec userSpec = new  UserProfileSpec()
				.setCountry("NG")
				.setTerritory("NG.05")
				.setCity(2351943)
				.setAddress("34, Victoria Street")
				.setEmail("pascal.ezeama." + Utils.newShortRandom() + "@gmail.com")
				.setFirstName("Pascal")
				.setMiddleName("Jazzman")
				.setLastName("Ezeama")
				.setPassword("passXYZ")
				.setGender(Gender.MALE)
				.setPhone(new Random().nextLong())
				.setImage(image)
				.setDateOfBirth(new Date(1996, 04, 19));
		
		return userSpec;
	}
}
