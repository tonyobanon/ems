package com.kylantis.eaa.core.fusion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.kylantis.eaa.core.users.Functionality;

import io.vertx.core.http.HttpMethod;

@Retention(RetentionPolicy.RUNTIME)
public @interface EndpointMethod {
	
	String uri();

	boolean createXhrClient() default true;
	
	boolean requireSSL() default false;
	
	boolean isBlocking() default false;
	
	boolean isAsync() default true;
	
	String[] headerParams() default {};

	String[] requestParams() default {};

	String[] bodyParams() default {};
	
	boolean enableMultipart() default false;
	
	boolean cache() default false;
	
	HttpMethod method() default HttpMethod.GET;

	Functionality functionality();
}
