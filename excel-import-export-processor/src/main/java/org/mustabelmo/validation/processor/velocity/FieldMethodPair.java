package org.mustabelmo.validation.processor.velocity;

import java.util.Comparator;

public class FieldMethodPair extends ExcelCellWrapper implements Comparable<FieldMethodPair> {
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
    public int compareTo(FieldMethodPair o) {
        return Comparator.comparing(FieldMethodPair::getOrder)
                .thenComparing(FieldMethodPair::getMethod)
                .thenComparing(FieldMethodPair::getField)
                .compare(this,  o);
    }
}
