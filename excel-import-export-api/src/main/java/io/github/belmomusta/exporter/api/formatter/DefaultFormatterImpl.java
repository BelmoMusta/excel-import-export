package io.github.belmomusta.exporter.api.formatter;

public class DefaultFormatterImpl implements Formatter<Object> {
	@Override
	public String format(Object object) {
		return String.valueOf(object);
	}
}
