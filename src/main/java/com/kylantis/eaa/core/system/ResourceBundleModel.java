package com.kylantis.eaa.core.system;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ce.ems.base.classes.InstallOptions;
import com.ce.ems.base.core.CacheType;
import com.ce.ems.base.core.Exceptions;
import com.ce.ems.base.core.Logger;
import com.ce.ems.base.core.ResourceScanner;
import com.ce.ems.base.core.Todo;
import com.ce.ems.models.BaseModel;
import com.ce.ems.models.ConfigModel;
import com.ce.ems.models.helpers.CacheHelper;
import com.ce.ems.utils.LocaleUtils;
import com.kylantis.eaa.core.keys.ConfigKeys;

public class ResourceBundleModel extends BaseModel {

	private static final CacheType defaultCacheType = CacheType.PERSISTENT;

	private static String ext = ".properties";
	private static Pattern pattern = Pattern.compile("_(a-z){2,3}((-)a-zA-Z){2,3}\\Q.properties\\E\\z");

	// K: Language, V: CountryCode ...
	private static final Map<String, List<String>> localeCountries = new HashMap<>();

	private static final Map<String, String> alternateCountries = new HashMap<>();

	private static final String BUNDLE_PREFIX = "rb_";

	@Override
	public String path() {
		return "core/resource_bundle";
	}

	@Override
	public void install(InstallOptions options) {

	}

	@Todo("Make RB entries persistent, add support for auto translation via google translate")
	@Override
	public void start() {

		// Read resource bundle files
		new ResourceScanner(ext).scan().forEach(p -> {

			boolean isBundle = false;
			String filePath = p.toString();
			Matcher m = pattern.matcher(filePath);

			while (m.find()) {

				isBundle = true;

				String match = m.group();

				Logger.debug("Saving resource bundle: " + match);

				// String bundleName = p.getFileName().toString().replace(match, "");

				String localeString = match.replace("_", "").replace(".properties", "");

				Locale locale = Locale.forLanguageTag(localeString);
				addLocale(locale);

				Properties bundle = new Properties();
				try {
					bundle.load(Files.newInputStream(p));
				} catch (IOException e) {
					Exceptions.throwRuntime(e);
				}

				Map<String, Object> entries = new HashMap<>();

				// Add To Cache
				bundle.forEach((k, v) -> {

					String key = checkKey(k.toString());
					entries.put(key, v);
				});

				putAll(localeString, entries);
			}

			if (!isBundle) {
				Logger.warn(filePath + " could not be identified as a resource bundle");
			}
		});

	}

	private void putAll(String locale, Map<String, Object> values) {
		CacheHelper.addToMapOrCreate(defaultCacheType, getKey(locale), values);
	}

	public static String _get(String localeString, String key) {
		Object value = CacheHelper.getMapEntry(defaultCacheType, getKey(localeString), key);
		return value != null ? value.toString() : null;
	}

	@Todo("Use a caching mechanism, @Cache for getting default config")
	public String get(String key) {
		return get(ConfigModel.get(ConfigKeys.DEFAULT_LOCALE), key);
	}

	public static String get(String localeString, String key) {

		key = checkKey(key);

		Object value = _get(localeString, key);

		if (value == null) {

			Locale locale = Locale.forLanguageTag(localeString);

			// Use the alternative country, if any

			if (alternateCountries.containsKey(locale.getCountry())) {

				return get(LocaleUtils.buildLocaleString(locale.getLanguage(),
						alternateCountries.get(locale.getCountry())), key);

			} else if (localeCountries.containsKey(locale.getLanguage())) {

				// From all available countries, find a suitable

				for (String country : localeCountries.get(locale.getLanguage())) {

					value = _get(LocaleUtils.buildLocaleString(locale.getLanguage(), country), key);

					if (value != null) {

						if (alternateCountries.containsKey(country)
								&& alternateCountries.get(country).equals(locale.getCountry())) {

							// <locale.getCountry()> is already the alternate country of <country>
							// Don't set as alternate country, to avoid a StackOverflowException happening in the future

						} else {

							// Set as alternate country
							alternateCountries.put(locale.getCountry(), country);
						}
						
						break;
					}
				}
			}
		}

		return value != null ? value.toString() : null;
	}

	public Map<String, Object> getAll(String locale) {
		return CacheHelper.getMap(defaultCacheType, getKey(locale));
	}

	private static String getKey(String locale) {
		return BUNDLE_PREFIX + locale;
	}

	private static String checkKey(String key) {

		if (key == null | key.trim().equals("")) {
			throw new NullPointerException();
		}

		return key;
	}

	private static void addLocale(Locale l) {
		if (!localeCountries.containsKey(l.getLanguage())) {
			localeCountries.put(l.getLanguage(), new ArrayList<>());
		}
		localeCountries.get(l.getLanguage()).add(l.getCountry());
	}

}
