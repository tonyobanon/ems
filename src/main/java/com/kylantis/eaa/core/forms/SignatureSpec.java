package com.kylantis.eaa.core.forms;

public class SignatureSpec {

	private final String tsaUrl;
	private final boolean isExternal;
	private final String certificate;
	private final String password;

	private String name;
	private String location;
	private String reason;
	private String contactInfo;

	public SignatureSpec(String tsaUrl, boolean isExternal, String certificate, String password) {
		this.tsaUrl = tsaUrl;
		this.isExternal = isExternal;
		this.certificate = certificate;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public SignatureSpec setName(String name) {
		this.name = name;
		return this;
	}

	public String getLocation() {
		return location;
	}

	public SignatureSpec setLocation(String location) {
		this.location = location;
		return this;
	}

	public String getReason() {
		return reason;
	}

	public SignatureSpec setReason(String reason) {
		this.reason = reason;
		return this;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public SignatureSpec setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
		return this;
	}

	public String getCertificate() {
		return certificate;
	}

	public String getPassword() {
		return password;
	}

	public String getTsaUrl() {
		return tsaUrl;
	}

	public boolean isExternal() {
		return isExternal;
	}

}
