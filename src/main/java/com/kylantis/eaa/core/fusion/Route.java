package com.kylantis.eaa.core.fusion;

import io.vertx.core.http.HttpMethod;

public class Route {

	private String uri;
	private HttpMethod method;

	public Route() {
	}
	
	public Route(String uri, HttpMethod method) {
		this.uri = uri;
		this.method = method;
	}

	public String getUri() {
		return uri;
	}

	public Route setUri(String uri) {
		this.uri = uri;
		return this;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public Route setMethod(HttpMethod method) {
		this.method = method;
		return this;
	}
	
	@Override
	public String toString() {
		return 
				 //Match all paths and methods
				this.getUri() == null && this.getMethod() == null ? "*" :
				// Match by method only 
				this.getUri() == null && this.getMethod() != null ? this.getMethod().name() :
				// Match by path only 
				this.getUri() != null && this.getMethod() == null ? this.getUri() :
				// Match by method and path 
				this.getUri() != null && this.getMethod() != null ? this.getUri() + "-" + this.getMethod().name() : "";
	}
}
