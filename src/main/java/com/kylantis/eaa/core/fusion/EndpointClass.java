package com.kylantis.eaa.core.fusion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EndpointClass {
	String uri();
}
