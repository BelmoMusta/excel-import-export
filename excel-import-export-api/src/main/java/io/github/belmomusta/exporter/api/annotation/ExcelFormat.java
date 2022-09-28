package io.github.belmomusta.exporter.api.annotation;

import io.github.belmomusta.exporter.api.formatter.Formatter;

public @interface ExcelFormat {
	Class<? extends Formatter> formatter();
}
