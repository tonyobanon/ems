package com.ce.ems.base.classes.gsonserializers;

import java.lang.reflect.Type;
import java.util.Date;

import com.ce.ems.utils.BackendObjectMarshaller;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateSerializer implements JsonSerializer<Date> {

	@Override
	public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(BackendObjectMarshaller.marshal(src));
	}

}
