package io.github.belmomusta.exporter.processor.velocity;

import java.util.Comparator;

@SuppressWarnings({"all", "used in velocity"})
public class FieldMethodPair extends ExcelCellWrapper implements Comparable<FieldMethodPair> {
    private final String field;
    private final String method;
    boolean isStaticMethod;
    FormatterWrapper formatterWrapper;
    private String headerName;
    
    public FieldMethodPair(String field, String method) {
        this.field = field;
        this.method = method;
    }
    
    public FieldMethodPair(String method) {
        this(method, method);
    }
    
    
    public String getMethod() {
        return method;
    }
    
    public String getField() {
        return field;
    }
    
    @Override
    public int compareTo(FieldMethodPair o) {
        Comparator<FieldMethodPair> insertionOrder = (a, b) -> 1;
        return Comparator.comparing(FieldMethodPair::getOrder)
                .thenComparing(insertionOrder)
                .compare(this, o);
    }
    
    public String getName() {
        return headerName;
    }
    
    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }
    
    public FormatterWrapper getFormatter() {
        return formatterWrapper;
    }
    
    public void setFormatter(String formatter) {
        formatterWrapper = new FormatterWrapper(formatter);
    }
    
    public boolean isStaticMethod() {
        return isStaticMethod;
    }
    
    public void setStaticMethod(boolean staticMethod) {
        isStaticMethod = staticMethod;
    }
    
    public String getCall(String className) {
        if (isStaticMethod)
            return className + "." + method + "()";
        return "object." + method + "()";
    }
}
