package com.ce.ems.base.classes.gsonserializers;

import java.lang.reflect.Type;

import com.ce.ems.base.classes.Gender;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GenderSerializer implements JsonSerializer<Gender> {

	@Override
	public JsonElement serialize(Gender src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.getValue());
	}

}
