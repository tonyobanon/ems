package com.kylantis.eaa.core.fusion;

import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.vertx.ext.web.Cookie;


public class CookieImpl implements Cookie {

  private final io.netty.handler.codec.http.cookie.Cookie nettyCookie;
  private boolean changed;
  private boolean fromUserAgent;

  public CookieImpl(String name, String value) {
    this.nettyCookie = new DefaultCookie(name, value);
    this.changed = true;
  }

  public CookieImpl(io.netty.handler.codec.http.cookie.Cookie nettyCookie) {
    this.nettyCookie = nettyCookie;
    fromUserAgent = true;
  }

  @Override
  public String getValue() {
    return nettyCookie.value();
  }

  @Override
  public Cookie setValue(final String value) {
    nettyCookie.setValue(value);
    this.changed = true;
    return this;
  }

  @Override
  public String getName() {
    return nettyCookie.name();
  }

  @Override
  public Cookie setDomain(final String domain) {
    nettyCookie.setDomain(domain);
    this.changed = true;
    return this;
  }

  @Override
  public String getDomain() {
    return nettyCookie.domain();
  }

  @Override
  public Cookie setPath(final String path) {
    nettyCookie.setPath(path);
    this.changed = true;
    return this;
  }

  @Override
  public String getPath() {
    return nettyCookie.path();
  }

  @Override
  public Cookie setMaxAge(final long maxAge) {
    nettyCookie.setMaxAge(maxAge);
    this.changed = true;
    return this;
  }
  
  public long getMaxAge() {
	return nettyCookie.maxAge();
  }

  @Override
  public Cookie setSecure(final boolean secure) {
    nettyCookie.setSecure(secure);
    this.changed = true;
    return this;
  }
  
  public boolean isSecure() {
		return nettyCookie.isSecure();
  }

  @Override
  public Cookie setHttpOnly(final boolean httpOnly) {
    nettyCookie.setHttpOnly(httpOnly);
    this.changed = true;
    return this;
  }
  
  public boolean isHttpOnyOnly() {
	return nettyCookie.isHttpOnly();
  }

  @Override
  public String encode() {
    return ServerCookieEncoder.STRICT.encode(nettyCookie);
  }

  @Override
  public boolean isChanged() {
    return changed;
  }

  @Override
  public void setChanged(boolean changed) {
    this.changed = changed;
  }

  @Override
  public boolean isFromUserAgent() {
    return fromUserAgent;
  }

}
