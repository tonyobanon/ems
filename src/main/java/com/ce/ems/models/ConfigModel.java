package com.ce.ems.models;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.ce.ems.base.classes.InstallOptions;
import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.base.core.Todo;
import com.ce.ems.entites.ConfigEntity;
import com.googlecode.objectify.Key;
import com.kylantis.eaa.core.keys.ConfigKeys;

@Todo("Add functionality, that allow incremental updates to config parameters from the frontend, "
		+ "i.e uploading a config file")
@BlockerTodo("Use Ibm Icu as the default locale provider")
public class ConfigModel extends BaseModel {

	@Override
	public String path() {
		return "core/config";
	}
	
	@Override
	public void install(InstallOptions options) {
		
		ConfigModel.put(ConfigKeys.ORGANIZATION_NAME, options.getCompanyName());
		ConfigModel.put(ConfigKeys.ORGANIZATION_LOGO_URL, options.getCompanyLogoUrl());
		ConfigModel.put(ConfigKeys.ORGANIZATION_COUNTRY, options.getCountry());
		ConfigModel.put(ConfigKeys.ORGANIZATION_AUDIENCE, options.getAudience());
		
		ConfigModel.put(ConfigKeys.ORGANIZATION_STUDENT_COUNT, options.getStudentCount());
		ConfigModel.put(ConfigKeys.ORGANIZATION_EMPLOYEE_COUNT, options.getEmployeeCount());
		
		ConfigModel.put(ConfigKeys.DEFAULT_CURRENCY, options.getCurrency());
		ConfigModel.put(ConfigKeys.DEFAULT_TIMEZONE, options.getTimezone());
		
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
