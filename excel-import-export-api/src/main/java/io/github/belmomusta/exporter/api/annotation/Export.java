package io.github.mustabelmo.exporter.api.annotation;

import io.github.mustabelmo.exporter.api.common.CommonExporter;
import io.github.mustabelmo.exporter.api.common.ExportType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Export {
	ExportType[] type();
	boolean ignoreHeaders() default true;
	boolean useFQNs() default false;
	String delimiter() default CommonExporter.COMMA;
	boolean withIoC() default false;
}