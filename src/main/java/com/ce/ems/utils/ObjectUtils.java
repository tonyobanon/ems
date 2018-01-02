package com.ce.ems.utils;

import java.util.Map;

import com.ce.ems.base.classes.FluentHashMap;
import com.googlecode.objectify.Key;

public class ObjectUtils {

	public static boolean isEmpty(Object obj) {

		switch (obj.getClass().getName()) {
		case "java.lang.String":
			return isStringEmpty((String) obj);
		default:
			return false;
		}
	}

	private static boolean isStringEmpty(String s) {
		return s.equals("") || s == null;
	}

	public static Map<String, String> toStringMap(Map<String, Object> o) {
		Map<String, String> result = new FluentHashMap<>();
		o.forEach((k, v) -> {
			result.put(k, v.toString());
		});
		return result;
	}
	
	public static String toKeyString(Key<?> k) {
		return k.getId() != 0 ? Long.valueOf(k.getId()).toString() : k.getName();
	}
}
