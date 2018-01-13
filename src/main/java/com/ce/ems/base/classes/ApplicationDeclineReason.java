package com.ce.ems.base.classes;

public enum ApplicationDeclineReason {

	INCOMPLETE_INFORMATION(1), INCONSISTENT_INFORMATION(2), UNVERIFIED_INFORMATION(3), DUPLICATE(4);

	private Integer value;

	private ApplicationDeclineReason(Integer value) {
		this.value = value;
	}

	public static ApplicationDeclineReason from(int value) {

		switch (value) {

		case 1:
			return ApplicationDeclineReason.INCOMPLETE_INFORMATION;
			
		case 2:
			return ApplicationDeclineReason.INCONSISTENT_INFORMATION;
			
		case 3:
			return ApplicationDeclineReason.UNVERIFIED_INFORMATION;
			
		case 4:
			return ApplicationDeclineReason.DUPLICATE;
			
		default:
			throw new IllegalArgumentException("An invalid value was provided");
		}
	}

	public Integer getValue() {
		return value;
	}
	
	@Override
	@ClientAware
	public String toString() {
		return "application_decline_reason." + value.toString();
	}
}
