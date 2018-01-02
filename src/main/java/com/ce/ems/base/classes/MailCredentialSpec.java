package com.ce.ems.base.classes;

public class MailCredentialSpec {

	private String providerUrl;
	private String username;
	private String password;
	
	public MailCredentialSpec(String providerUrl, String username, String password) {
		this.providerUrl = providerUrl;
		this.username = username;
		this.password = password;
	}

	public String getProviderUrl() {
		return providerUrl;
	}

	public void setProviderUrl(String providerUrl) {
		this.providerUrl = providerUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
