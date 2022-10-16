package io.github.belmomusta.exporter.api.annotation;

import io.github.belmomusta.exporter.api.common.CommonExporter;

public @interface ExportProperties {
	boolean ignoreHeaders() default true;
	boolean useFQNs() default false;
	String delimiter() default CommonExporter.COMMA;
	boolean withIoC() default false;
}
