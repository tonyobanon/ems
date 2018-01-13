package com.kylantis.eaa.core.gaefusion;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.LanguageHeader;
import io.vertx.ext.web.Locale;
import io.vertx.ext.web.ParsedHeaderValues;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

public class GAERouteContext implements RoutingContext {

	private GAEHttp2ServerRequestMock request;
	private GAEHttp2ServerResponseMock response = new GAEHttp2ServerResponseMock();

	public GAERouteContext(HttpServletRequest req) {
		request = new GAEHttp2ServerRequestMock(req);
	}

	@Override
	public @Nullable Cookie getCookie(String name) {
		return request.cookies().get(name);
	}

	@Override
	public RoutingContext addCookie(Cookie cookie) {
		response.addCookie(cookie);
		return this;
	}

	@Override
	public @Nullable Cookie removeCookie(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int cookieCount() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Cookie> cookies() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getBodyAsString() {
		return getBody() != null ? getBody().toString() : null;
	}

	@Override
	public String getBodyAsString(String encoding) {
		return getBody() != null ? getBody().toString(encoding) : null;
	}

	@Override
	public JsonObject getBodyAsJson() {
		return getBody() != null ? new JsonObject(getBody().toString()) : null;
	}

	@Override
	public JsonArray getBodyAsJsonArray() {
		return getBody() != null ? new JsonArray(getBody().toString()) : null;
	}

	@Override
	public Buffer getBody() {
		return request.body;
	}

	@Override
	public Set<FileUpload> fileUploads() {
		return request.fileUploads();
	}

	@Override
	public GAEHttp2ServerResponseMock response() {
		return response;
	}

	@Override
	public HttpServerRequest request() {
		return request;
	}

	/**
	 * This is usually called by the auth handler to indicate that auth was
	 * successfully. In this context, this is a no-op
	 */
	@Override
	public void next() {
	}

	@Override
	public void fail(int statusCode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void fail(Throwable throwable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public RoutingContext put(String key, Object obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T get(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T remove(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, Object> data() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Vertx vertx() {
		throw new UnsupportedOperationException();
	}

	@Override
	public @Nullable String mountPoint() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Route currentRoute() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String normalisedPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public @Nullable Session session() {
		throw new UnsupportedOperationException();
	}

	@Override
	public @Nullable User user() {
		throw new UnsupportedOperationException();
	}

	@Override
	public @Nullable Throwable failure() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int statusCode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public @Nullable String getAcceptableContentType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ParsedHeaderValues parsedHeaders() {
		return request.parsedHeaders();
	}

	@Override
	public int addHeadersEndHandler(Handler<Void> handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeHeadersEndHandler(int handlerID) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int addBodyEndHandler(Handler<Void> handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeBodyEndHandler(int handlerID) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean failed() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setBody(Buffer body) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSession(Session session) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setUser(User user) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearUser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAcceptableContentType(@Nullable String contentType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void reroute(String path) {
		this.response.setStatusCode(HttpServletResponse.SC_FOUND).putHeader("Location", path).end();
	}

	@Override
	public void reroute(HttpMethod method, String path) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Locale> acceptableLocales() {
		return request.locales();
	}
	
	@Override
	public List<LanguageHeader> acceptableLanguages() {
		return request.getLanguageHeaders();
	}

	@Override
	public Map<String, String> pathParams() {
		throw new UnsupportedOperationException();
	}

	@Override
	public @Nullable String pathParam(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MultiMap queryParams() {
		return request.params();
	}

	@Override
	public @Nullable List<String> queryParam(String query) {
		return request.params().getAll(query);
	}
}
