package io.github.belmomusta.exporter.api.annotation;

import io.github.belmomusta.exporter.api.formatter.Formatter;

public @interface ColumnFormat {
	Class<? extends Formatter> formatter();
}
