package com.kylantis.eaa.core.gaefusion;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import com.ce.ems.base.core.Todo;

import io.vertx.ext.web.LanguageHeader;
import io.vertx.ext.web.ParsedHeaderValue;

@Todo("In this package, class don't throw exceptions via ")
public class LanguageHeaderImpl implements LanguageHeader {

	private final Locale locale;
	
	public LanguageHeaderImpl(Locale locale) {
		this.locale = locale;
	}
	
	@Override
	public String rawValue() {
		return locale.toString();
	}

	@Override
	public String value() {
		return locale.toString();
	}

	@Override
	public String language() {
		return locale.getLanguage();
	}

	@Override
	public String country() {
		return locale.getCountry();
	}

	@Override
	public String variant() {
		return locale.getVariant();
	}

	@Override
	public String tag() {
		return locale.toLanguageTag();
	}

	@Override
	public String subtag() {
		return locale.toLanguageTag();
	}

	@Override
	public String subtag(int level) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int subtagCount() {
		throw new UnsupportedOperationException();
	}

	@Override
	public float weight() {
		return 1.0f;
	}

	@Override
	public String parameter(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, String> parameters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPermitted() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isMatchedBy(ParsedHeaderValue matchTry) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends ParsedHeaderValue> T findMatchedBy(Collection<T> matchTries) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int weightedOrder() {
		throw new UnsupportedOperationException();
	}

}
