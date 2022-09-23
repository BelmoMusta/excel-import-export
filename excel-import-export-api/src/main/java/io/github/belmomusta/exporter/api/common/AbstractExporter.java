package io.github.belmomusta.exporter.api.common;

import io.github.belmomusta.exporter.api.excel.ExcelExporter;
import io.github.belmomusta.exporter.api.exception.ExporterException;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public abstract class AbstractExporter<T> implements ExcelExporter<T> {
    
    @Override
    public final void exportToFile(T object, File destFile) throws ExporterException {
        exportToFile(Collections.singletonList(object), destFile);
    }
    
    @Override
    public final void exportToFile(T object, String destFile) throws ExporterException {
        exportToFile(object, new File(destFile));
    }
    
    @Override
    public final void exportToFile(Collection<? extends T> objects, String destFile) throws ExporterException {
        exportToFile(objects, new File(destFile));
    }
    
    protected final String valueOf(Object object) {
        return Optional.ofNullable(object)
                .map(String::valueOf)
                .orElse(EMPTY_STRING);
    }
}
