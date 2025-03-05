package io.github.mustabelmo.export.examples.objects;

import io.github.mustabelmo.exporter.api.annotation.Export;
import io.github.mustabelmo.exporter.api.annotation.ToColumn;
import io.github.mustabelmo.exporter.api.common.ExportType;

@Export(type = ExportType.EXCEL)
public enum MyEnumeration {
    
    A,
    B,
    
    ;
    @ToColumn
    String value;
    
    public String getValue() {
        return this.name();
    }
}
