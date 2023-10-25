package io.github.belmomusta.exporter.processor.velocity;

public class ExcelCellWrapper {
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
}
