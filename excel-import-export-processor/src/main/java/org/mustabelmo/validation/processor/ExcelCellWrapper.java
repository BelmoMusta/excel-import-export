package org.mustabelmo.validation.processor;

import java.util.Comparator;

public class ExcelCellWrapper implements Comparable<ExcelCellWrapper> {
    private int order;


    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isOrdered() {
        return order != Integer.MAX_VALUE;
    }

    @Override
    public int compareTo(ExcelCellWrapper o) {
        return Comparator.comparing(ExcelCellWrapper::getOrder).compare(this, o);
    }
}
