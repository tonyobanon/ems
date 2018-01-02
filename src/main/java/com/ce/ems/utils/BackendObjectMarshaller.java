package com.ce.ems.utils;

import java.util.Date;

public class BackendObjectMarshaller {

	/* Boolean */
	public static String marshal(Boolean object) {
		return object.equals(Boolean.TRUE) ? "1" : "0";
	}

	public static Boolean unmarshalBool(Object object) {
		return object != null && object.equals("1") ? Boolean.TRUE : Boolean.FALSE;
	}

	/* Date */
	public static String marshal(Date object) {
		return Dates.toString(object);
	}

	public static Date unmarshalDate(String object) {
		return Dates.toDate(object);
	}

}
