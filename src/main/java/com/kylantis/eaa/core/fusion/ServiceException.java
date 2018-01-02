package com.kylantis.eaa.core.fusion;

import com.ce.ems.base.core.ResourceException;

public class ServiceException extends ResourceException {

	public ServiceException(int errCode) {
		super(errCode);
	}
	
	public ServiceException(int errCode, String msg) {
		super(errCode, msg);
	}

	private static final long serialVersionUID = 1L;
	
}
