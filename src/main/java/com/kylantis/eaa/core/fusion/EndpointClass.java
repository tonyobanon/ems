package com.kylantis.eaa.core.fusion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.ce.ems.models.BaseModel;

@Retention(RetentionPolicy.RUNTIME)
public @interface EndpointClass {
	String uri();
	Class<? extends BaseModel>[] externalModels() default {};
}
