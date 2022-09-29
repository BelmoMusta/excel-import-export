package io.github.belmomusta.exporter.api.formatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter implements Formatter<Number> {
	@Override
	public String format(Number object) {
		DecimalFormat currencyInstance = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
		currencyInstance.setMinimumFractionDigits(2);
		return currencyInstance.format(object);
 	}
}
