package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.Export;
import io.github.belmomusta.exporter.api.annotation.ToColumn;
import io.github.belmomusta.exporter.api.common.ExportType;

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
