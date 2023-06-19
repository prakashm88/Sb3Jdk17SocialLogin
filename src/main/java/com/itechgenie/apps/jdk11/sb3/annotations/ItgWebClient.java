package com.itechgenie.apps.jdk11.sb3.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
@Documented
public @interface ItgWebClient  {
	String id() default "app-api-service";

	String url();

	//String method();

	//String body();

	boolean isBlocking() default false;

	String[] headers();

}
