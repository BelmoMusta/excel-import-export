package io.github.belmomusta.exporter.api.formatter;

public interface Formatter<T> {
	String format(T object);
}
