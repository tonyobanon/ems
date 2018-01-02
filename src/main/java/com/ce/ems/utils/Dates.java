package com.ce.ems.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ce.ems.base.core.Exceptions;


public class Dates {
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	public static Date now(){
		return new Date();
	}
	
	public static String currentDate(){
		return format.format(Calendar.getInstance().getTime());
	}
	
	public static String toString(Date o) {
		return format.format(o);
	}

	public static Date toDate(String o) {
		try {
			return format.parse(o);
		} catch (ParseException e) {
			Exceptions.throwRuntime(e);
			return null;
		}
	}
	
}
