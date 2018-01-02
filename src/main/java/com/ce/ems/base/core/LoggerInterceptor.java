package com.ce.ems.base.core;

import java.util.Collection;

public interface LoggerInterceptor {

	void takeSnapshot(Collection<String> logEntries);
	
}
