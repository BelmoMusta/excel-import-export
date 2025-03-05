package io.github.mustabelmo.exporter.api.annotation;

import io.github.mustabelmo.exporter.api.common.CommonExporter;

public @interface ExportProperties {
	boolean ignoreHeaders() default true;
	boolean useFQNs() default false;
	String delimiter() default CommonExporter.COMMA;
	boolean withIoC() default false;
}
