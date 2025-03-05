package io.github.mustabelmo.export.examples.objects;

import io.github.mustabelmo.exporter.api.annotation.Export;
import io.github.mustabelmo.exporter.api.annotation.ToColumn;
import io.github.mustabelmo.exporter.api.common.ExportType;

@Export(type = ExportType.EXCEL, ignoreHeaders = false)
public interface InterfaceToExcel {
    @ToColumn
    static String toto() {
        return "toto";
    }
    
    @ToColumn
    String getSomething();
}
