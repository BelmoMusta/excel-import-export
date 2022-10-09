package io.github.belmomusta.exporter.api.annotation;

import io.github.belmomusta.exporter.api.formatter.Formatter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnFormat {
	Class<? extends Formatter<?>> formatter();
}
