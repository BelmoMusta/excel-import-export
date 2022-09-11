package org.mustabelmo.validation.processor;

public class Header extends ExcelCellWrapper {
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
}
