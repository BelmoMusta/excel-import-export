package org.mustabelmo.validation.processor;

import java.util.Comparator;
import java.util.function.Function;

public class Header extends ExcelCellWrapper implements Comparable<Header> {
    private final String name;

    public Header(String name, int order) {
        this.name = name;
        super.setOrder(order);
    }


    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public int compareTo(Header o) {
        return Comparator.comparing(Header::getOrder)
                .thenComparing(Header::getName)
                .compare(this, o);
    }
}
