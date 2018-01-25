package com.ce.ems.base.core;

@BlockerTodo("Add metrics to indicate application error counts")
public class Exceptions {

	private static final Boolean PRINT_STACK_ON_PRODUCTION_ERROR = true;
	
	private static void throwRuntime(String msg) {
		//Call the Logger utility here to output error messages ..
		throw new RuntimeException(msg);
	}

	public static void throwRuntime(Throwable t) {
		
		if(Application.isProduction() && PRINT_STACK_ON_PRODUCTION_ERROR) {
			//Print stack trace
			t.printStackTrace();
		}
		
		//Call the Logger utility here to output error messages ..
		throw new RuntimeException(t);
	}

	public static void throwRuntime(Integer errorCode, Object ref1, Object ref2, Throwable t) {
		String msg = ErrorMessages.get(errorCode, ref1, ref2);
		throwRuntime(msg);
	}

	public static void throwRuntime(Integer errorCode, Object ref1, Object ref2) {
		throwRuntime(errorCode, ref1, ref2, null);
	}

	public static void throwRuntime(Integer errorCode, Object ref) {
		throwRuntime(errorCode, ref, null, null);
	}

	public static void throwRuntime(Integer errorCode) {
		throwRuntime(errorCode, null, null, null);
	}

}
