package io.github.mustabelmo.exporter.api.formatter;

public interface Formatter<T> {
	String format(T object);
}
