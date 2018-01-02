package com.ce.ems.base.classes.gsonserializers;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.kylantis.eaa.core.users.RoleRealm;

public class RoleRealmSerializer implements JsonSerializer<RoleRealm> {

	@Override
	public JsonElement serialize(RoleRealm src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.getValue());
	}

}
