package com.ce.ems.base.core;

public class ErrorMessages {

	public static String get(Integer errorCode, Object ref1, Object ref2) {
		String msg = null;

		// Add error codes here
		switch (errorCode) {
		case 15:
			msg = "Base directory could not be set by the Classpath Scanner, becuase this app is packaged in a Jar archive";
			break;
		case 16:
			msg = "Error occured while instantiating " + ref1;
			break;
		case 17:
			msg = "Circular dependency for tree: " + ref1;
			break;
		}

		return "ERRCODE " + errorCode + ": " + msg;
	}

}
