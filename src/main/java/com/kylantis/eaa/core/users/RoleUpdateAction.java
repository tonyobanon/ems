package com.kylantis.eaa.core.users;

public enum RoleUpdateAction {

	ADD(1), REMOVE(0);

	private final Integer action;

	private RoleUpdateAction(int action) {
		this.action = action;
	}

	public Integer getAction() {
		return action;
	}

	public static RoleUpdateAction from(int action) {
		switch (action) {
		case 0:
			return REMOVE;
		case 1:
		default:
			return ADD;
		}
	}

}
