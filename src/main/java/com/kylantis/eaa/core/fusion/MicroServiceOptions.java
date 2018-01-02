package com.kylantis.eaa.core.fusion;

public class MicroServiceOptions {

	private boolean enableCors;
	private Integer port;

	public MicroServiceOptions() {
	}

	public Integer getPort() {
		return port;
	}

	/*
	 * public MicroServiceOptions withBaseURI(String baseURI) { if
	 * (!MicroserviceContainer.uriPattern.matcher(baseURI).matches()) { throw
	 * new RuntimeException(
	 * "Incorrect pattern for base URI, example of a correct pattern is: /base or /base1/base2"
	 * ); } this.baseURI = baseURI; return this; }
	 */

	public MicroServiceOptions withPort(Integer port) {
		this.port = port;
		return this;
	}


	public MicroServiceOptions enableCors() {
		this.enableCors = true;
		return this;
	}

	public MicroServiceOptions disableCors() {
		this.enableCors = false;
		return this;
	}

	public boolean isCorsEnabled() {
		return this.enableCors;
	}

}
