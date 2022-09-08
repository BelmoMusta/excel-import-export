package io.github.belmomusta.excel.importexport;

import java.util.Collection;

public class ExcelExporterAnnotation<T> extends ExcelExporter<T> {

    public static <R> ExcelExporterAnnotation<R> exportContent(Collection<R> content) {
        ExcelExporterAnnotation<R> rExcelExporterService = new ExcelExporterAnnotation<>();
        rExcelExporterService.content = content;
        if (!content.isEmpty()) {
            R next = content.iterator().next();
            rExcelExporterService.mapAnnotatedMembers(next.getClass());
        }
        return rExcelExporterService;
    }
}

