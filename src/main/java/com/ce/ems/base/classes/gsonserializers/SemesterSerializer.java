package com.ce.ems.base.classes.gsonserializers;

import java.lang.reflect.Type;

import com.ce.ems.base.classes.Semester;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SemesterSerializer implements JsonSerializer<Semester> {

	@Override
	public JsonElement serialize(Semester src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.getValue());
	}

}
