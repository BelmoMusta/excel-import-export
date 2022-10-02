package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.formatter.Formatter;

public class IdConverter implements Formatter<Integer> {
	@Override
	public String format(Integer object) {
		return String.valueOf(object+2);
	}
}
