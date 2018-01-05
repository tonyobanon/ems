package com.ce.ems.base.core;

import com.ce.ems.base.classes.SystemErrorCodes;

public class SystemValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public SystemValidationException(SystemErrorCodes reason) {
		super(reason.getMessage());
	}
	
	public SystemValidationException(SystemErrorCodes reason, String ref) {
		super(reason.getMessage() + " (" + ref + ") ");
	}

}
