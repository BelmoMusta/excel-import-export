package org.mustabelmo.validation.processor;

import java.util.Comparator;

public class FieldMethodPair extends ExcelCellWrapper {
    private final String field;
    private final String method;

    public FieldMethodPair(String field, String method) {
        this.field = field;
        this.method = method;
    }


    @SuppressWarnings({"all","used in velocity"})
    public String getMethod() {
        return method;
    }

    public String getField() {
        return field;
    }

    @Override
    public int compareTo(ExcelCellWrapper o) {
        return Comparator.comparing(FieldMethodPair::getOrder)
                .thenComparing(FieldMethodPair::getField)
                .compare(this, (FieldMethodPair) o);
    }
}
