package com.ce.ems.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ce.ems.base.core.Exceptions;

public class FrontendObjectMarshaller {

	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	/* Date */
	public static String marshal(Date object) {
		return format.format(object);
	}

	public static Date unmarshalDate(String object) {
		try {
			return format.parse(object);
		} catch (ParseException e) {
			Exceptions.throwRuntime(e);
			return null;
		}
	}
}
