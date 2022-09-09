package org.mustabelmo.validation.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class FieldMethodPair implements Comparable<FieldMethodPair> {
    private int order;
    private final String field;
    private final String method;
    private Collection<String> headers = new ArrayList<>();

    public FieldMethodPair(String field, String method) {
        this.field = field;
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public String getField() {
        return field;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public int compareTo(FieldMethodPair o) {
        return Comparator.comparing(FieldMethodPair::getOrder).compare(this, o);
    }

    public void addHeader(String header) {
        headers.add(header);
    }
}
