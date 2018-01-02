package com.ce.ems.base.core;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

import com.ce.ems.base.classes.Semester;
import com.ce.ems.base.classes.gsonserializers.IndexedNameTypeDeserializer;
import com.ce.ems.base.classes.gsonserializers.IndexedNameTypeSerializer;
import com.ce.ems.base.classes.gsonserializers.RoleRealmDeserializer;
import com.ce.ems.base.classes.gsonserializers.RoleRealmSerializer;
import com.ce.ems.base.classes.gsonserializers.SemesterDeserializer;
import com.ce.ems.base.classes.gsonserializers.SemesterSerializer;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.kylantis.eaa.core.users.RoleRealm;

public class GsonFactory {

	private static Gson instance;
	
	public static Gson newInstance() {
		return instance;
	}

	public static <V> Map<String, String> toMap(JsonObject obj){
		Map<String, String> entries = new HashMap<>(obj.size());
		obj.entrySet().forEach((e) -> {
			entries.put(e.getKey(), e.getValue().getAsString());
		});
		return entries;
	}

	static {
		
		instance = new GsonBuilder()
				
				.enableComplexMapKeySerialization()
				//.serializeNulls()
				
				.setDateFormat(DateFormat.LONG)
				.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
				.setPrettyPrinting()
				
				//.setVersion(1.0)
				
				.registerTypeAdapter(Semester.class, new SemesterSerializer())
				.registerTypeAdapter(Semester.class, new SemesterDeserializer())
				
				.registerTypeAdapter(RoleRealm.class, new RoleRealmSerializer())
				.registerTypeAdapter(RoleRealm.class, new RoleRealmDeserializer())
				
				.registerTypeAdapter(RoleRealm.class, new IndexedNameTypeSerializer())
				.registerTypeAdapter(RoleRealm.class, new IndexedNameTypeDeserializer())
				
				.create();
	}
	
	private static class EnumSerializer implements JsonSerializer<Enum<?>> {
		
		@Override
		public JsonElement serialize(Enum<?> src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(Integer.toString(src.ordinal()));
		}	
		
	}
	
}
