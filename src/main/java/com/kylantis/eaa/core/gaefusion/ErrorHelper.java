package com.kylantis.eaa.core.gaefusion;

public class ErrorHelper {

	/**
	 * In our models, we don't explicitly catch exceptions for entities, therefore
	 * the throwable bubbles up to BaseApiServlet, and GAE wraps the throwable as an
	 * InvocationTargetException. This function then finds the real exception that
	 * occurred, before it was wrapped, and displays a returns a suitable error
	 * message
	 */
	public static Throwable getError(Exception e) {

		Throwable t = recurseCause(e);
		String className = t.getClass().getName();

		switch (className) {
		
		case "com.googlecode.objectify.NotFoundException":
			return new RuntimeException("No entity was found with the specified key.");
			
			
		default:
			return t;
		}
	}

	private static Throwable recurseCause(Throwable t) {

		if (t.getCause() != null) {
			return recurseCause(t.getCause());
		} else {
			return t;
		}

	}

}
