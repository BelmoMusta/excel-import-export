package io.github.belmomusta.exporter.api.common;

import io.github.belmomusta.exporter.api.exception.ExporterException;

import java.io.File;
import java.util.Collection;

public interface CommonExporter<T> {
    
    String EMPTY_STRING = "";
    String DOUBLE_QUOTES = "\"";
    String COMMA = ",";
    String SINGLE_QUOTE = "'";
    String ESCAPED_DOUBLE_QUOTES = "\"\"";
    String SPACE = " ";
    String LINE_BREAK = "\\R";
    
    void exportToFile(T object, String destFile) throws ExporterException;
    void exportToFile(T object, File destFile) throws ExporterException;
    void exportToFile(Collection<? extends T> objects, String destFile) throws ExporterException;
    void exportToFile(Collection<? extends T> objects, File destFile) throws ExporterException;
    Class<? extends T> getClassClass();
}
