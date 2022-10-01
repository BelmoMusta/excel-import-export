package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.formatter.Formatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter implements Formatter<Date> {
	@Override
	public String format(Date object) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(object);
 	}
}
