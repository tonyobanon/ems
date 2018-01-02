package com.kylantis.eaa.core.gaefusion;

import io.vertx.ext.web.Locale;

public class LocaleImpl implements Locale {

	private final String language;
	private final String country;
	private final String variant;
	
	public LocaleImpl(String language, String country, String variant) {
		this.language = language;
		this.country = country;
		this.variant = variant;
	}

	@Override
	public String language() {
		return language;
	}

	@Override
	public String country() {
		return country;
	}

	@Override
	public String variant() {
		return variant;
	}

}
