package org.mustabelmo.validation.processor.velocity;

import java.util.Comparator;
@SuppressWarnings({"all","used in velocity"})
public class FieldMethodPair extends ExcelCellWrapper implements Comparable<FieldMethodPair> {
    private final String field;
    private final String method;
    private  String headerName;

    public FieldMethodPair(String field, String method) {
        this.field = field;
        this.method = method;
    }
    
    public FieldMethodPair( String method) {
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
}
