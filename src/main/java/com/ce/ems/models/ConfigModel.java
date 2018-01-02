package com.ce.ems.models;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.ce.ems.base.core.Todo;
import com.ce.ems.entites.ConfigEntity;
import com.googlecode.objectify.Key;

@Todo("Add functionality, that allow incremental updates to config parameters from the frontend, "
		+ "i.e uploading a config file")
public class ConfigModel extends BaseModel {

	@Override
	public String path() {
		return "core/config";
	}
	
	@Override
	public void preInstall() {
		
		
	}
	
	public static String get(String key) {
		ConfigEntity e = ((ConfigEntity) ofy().load().key(Key.create(ConfigEntity.class, key)).now());
		return e != null ? e.getValue() : null;
	}

	public static Object put(String key, Object value) {
		if (value == null) {
			return null;
		}
		ofy().save().entity(new ConfigEntity().setKey(key).setValue(value)).now();
		return value;
	}

	public static void delete(String key) {
		ofy().delete().key(Key.create(ConfigEntity.class, key)).now();
	}

}
