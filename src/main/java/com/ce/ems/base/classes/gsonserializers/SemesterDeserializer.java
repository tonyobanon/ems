package com.ce.ems.base.classes.gsonserializers;

import java.lang.reflect.Type;

import com.ce.ems.base.classes.Semester;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class SemesterDeserializer implements JsonDeserializer<Semester> {

	@Override
	public Semester deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return Semester.from(json.getAsInt());
	}
}
