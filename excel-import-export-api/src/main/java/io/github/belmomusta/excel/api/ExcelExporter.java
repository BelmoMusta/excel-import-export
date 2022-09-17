package io.github.belmomusta.excel.api;

import io.github.belmomusta.excel.api.exception.ExcelExporterException;

import java.io.File;
import java.util.Collection;

public interface ExcelExporter<T> {
    void exportToFile(T t, String destFile) throws ExcelExporterException ;
    void exportToFile(T t, File destFile) throws ExcelExporterException ;
    void exportToFile(Collection<? extends T> t, String destFile) throws ExcelExporterException ;
    void exportToFile(Collection<? extends T> t, File destFile) throws ExcelExporterException ;
}
