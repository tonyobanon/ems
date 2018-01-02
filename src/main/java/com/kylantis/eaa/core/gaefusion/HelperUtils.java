package com.kylantis.eaa.core.gaefusion;

import com.kylantis.eaa.core.fusion.CookieImpl;

import io.vertx.ext.web.Cookie;

public class HelperUtils {

	public static Cookie toVertxCookie(javax.servlet.http.Cookie c) {

		Cookie cookie = new CookieImpl(c.getName(), c.getValue());

		if (c.getDomain() != null) {
			cookie.setDomain(c.getDomain());
		}
		cookie.setMaxAge(c.getMaxAge()).setSecure(c.getSecure()).setHttpOnly(c.isHttpOnly());

		if (c.getPath() != null) {
			cookie.setPath(c.getPath());
		}

		return cookie;
	}

	public static javax.servlet.http.Cookie toServletCookie(CookieImpl c) {

		javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(c.getName(), c.getValue());

		if (c.getDomain() != null) {
			cookie.setDomain(c.getDomain());
		}

		cookie.setMaxAge((int) c.getMaxAge());
		cookie.setSecure(c.isSecure());
		cookie.setHttpOnly(c.isHttpOnyOnly());

		if (c.getPath() != null) {
			cookie.setPath(c.getPath());
		}

		return cookie;
	}
}
