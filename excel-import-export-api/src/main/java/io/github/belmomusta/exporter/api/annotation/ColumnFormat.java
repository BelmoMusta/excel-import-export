package io.github.mustabelmo.exporter.api.annotation;

import io.github.mustabelmo.exporter.api.formatter.Formatter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnFormat {
	Class<? extends Formatter<?>> formatter();
}
