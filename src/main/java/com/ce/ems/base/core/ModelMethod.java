package com.ce.ems.base.core;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.kylantis.eaa.core.users.Functionality;

@Retention(SOURCE)
@Target(METHOD)
public @interface ModelMethod {

	Functionality[] functionality();

}
