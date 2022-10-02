package io.github.belmomusta.exporter.api.common;

import io.github.belmomusta.exporter.api.exception.ExporterException;
import io.github.belmomusta.exporter.api.formatter.Formatter;

import java.io.File;
import java.util.Collection;
import java.util.List;

public interface CommonExporter<T> {
    
    String EMPTY_STRING = "";
    String DOUBLE_QUOTES = "\"";
    String COMMA = ",";
    String SINGLE_QUOTE = "'";
    String ESCAPED_DOUBLE_QUOTES = "\"\"";
    String SPACE = " ";
    String LINE_BREAK = "\\R";
    
    List<String> getHeadersEntries();
    List<String> getRowEntries(T object);
    <X> String valueOf(X object);
    <X> String valueOf(X object, Formatter<X> formatter);
    void exportToFile(T object, String destFile) throws ExporterException;
    void exportToFile(T object, File destFile) throws ExporterException;
    void exportToFile(Collection<? extends T> objects, String destFile) throws ExporterException;
    void exportToFile(Collection<? extends T> objects, File destFile) throws ExporterException;
    Class<? extends T> getClassName();
}
