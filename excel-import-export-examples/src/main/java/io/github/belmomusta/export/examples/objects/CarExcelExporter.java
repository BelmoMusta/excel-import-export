package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.excel.AbstractExcelExporter;
import io.github.belmomusta.exporter.api.formatter.Formatter;

import java.util.Arrays;
import java.util.List;

public class CarExcelExporter extends AbstractExcelExporter<Car> {
    protected final Formatter lDateFormatter = new DateFormatter();
    protected final Formatter lCurrencyFormatter = new CurrencyFormatter();

    @Override
    protected List<String> getRowEntries(Car object) {
        return Arrays.asList(
            valueOf(object.getModel()),
            valueOf(object.getId()),
            valueOf(object.getFoo()),
            valueOf(object.getCreationDate(), lDateFormatter),
            valueOf(object.getUpdateDate(), lDateFormatter),
            valueOf(object.getPrice(), lCurrencyFormatter),
            valueOf(object.getName()));
    }

    @Override
    public Class<? extends Car> getClassName() {
        return Car.class;
    }
}
