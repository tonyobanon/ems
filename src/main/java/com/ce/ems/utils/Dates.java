package com.ce.ems.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ce.ems.base.core.BlockerTodo;

@BlockerTodo("Here and global.js, stop using the system's default timezone offset. Instead use platform configured timezone")
public class Dates {
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

	public static Date now(){
		return new Date();
	}
	
	public static String currentDate(){
		return format.format(Calendar.getInstance().getTime());
	}
	
	public static String toString(Date o) {
		return format.format(o);
	}

	public static Date toDate(String o) throws ParseException {
			return format.parse(o);
	}
	
	static {
		
	}
	
}
