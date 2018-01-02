package com.ce.ems.base.classes.gsonserializers;

import java.lang.reflect.Type;

import com.ce.ems.base.classes.IndexedNameType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class IndexedNameTypeDeserializer implements JsonDeserializer<IndexedNameType> {

	@Override
	public IndexedNameType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return IndexedNameType.from(json.getAsInt());
	}

}
