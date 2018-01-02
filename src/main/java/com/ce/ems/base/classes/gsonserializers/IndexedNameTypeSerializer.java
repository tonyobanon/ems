package com.ce.ems.base.classes.gsonserializers;

import java.lang.reflect.Type;

import com.ce.ems.base.classes.IndexedNameType;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class IndexedNameTypeSerializer implements JsonSerializer<IndexedNameType> {

	@Override
	public JsonElement serialize(IndexedNameType src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.getValue());
	}

}
