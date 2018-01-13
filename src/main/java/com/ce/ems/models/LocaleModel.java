package com.ce.ems.models;

import java.util.List;
import java.util.Locale;

import com.ce.ems.base.classes.InstallOptions;
import com.ce.ems.base.core.Model;
import com.ce.ems.base.core.RequestThreadAttributes;
import com.ce.ems.utils.LocaleUtils;
import com.kylantis.eaa.core.keys.ConfigKeys;

@Model(dependencies = ConfigModel.class)
public class LocaleModel extends BaseModel {

	public static final String USER_DEFAULT_LOCALE = "USER_DEFAULT_LOCALE";
	
	@Override
	public String path() {
		return "core/locale";
	}

	@Override
	public void install(InstallOptions options) {

		Locale locale = Locale.forLanguageTag(LocaleUtils.buildLocaleString(options.getLanguage(), options.getCountry()));
		ConfigModel.put(ConfigKeys.DEFAULT_LOCALE, locale.toLanguageTag());
	}
	
	@Override
	public void start() {
	}
	
	public static String defaultLocale() {
		return ConfigModel.get(ConfigKeys.DEFAULT_LOCALE);
	}
	
	public static void setDefaultLocale(Locale locale) {
		ConfigModel.put(ConfigKeys.DEFAULT_LOCALE, locale.toString());
	}	
	
	public static void setUserLocale(List<String> acceptableLocales) {		
		String defaultLocal = !acceptableLocales.isEmpty() ? acceptableLocales.get(0) : LocaleModel.defaultLocale();						
		Locale locale = Locale.forLanguageTag(defaultLocal);
		RequestThreadAttributes.set(USER_DEFAULT_LOCALE, locale.toLanguageTag());			
	}
	
	public static String getUserLocale() {
		return RequestThreadAttributes.get(USER_DEFAULT_LOCALE).toString();
	}
	
}
