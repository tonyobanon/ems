package com.ce.ems.base.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ce.ems.models.BaseModel;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Model {
	
	boolean enabled() default true;

	String version() default BaseModel.DEFAULT_MODEL_VERSION;
	
	Class<? extends BaseModel>[] dependencies() default {};
	
}
