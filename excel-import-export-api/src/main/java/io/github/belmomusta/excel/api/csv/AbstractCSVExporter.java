package io.github.belmomusta.excel.api.csv;

import io.github.belmomusta.excel.api.CSVExporter;
import io.github.belmomusta.excel.api.exception.ExcelExporterException;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractCSVExporter<T> implements CSVExporter<T> {
    
    public static final String EMPTY_STRING = "";
    
    @Override
    public final void exportToFile(T t, File destFile) throws ExcelExporterException{
        exportToFile(Collections.singletonList(t), destFile);
    }
    
    @Override
    public final void exportToFile(T t, String destFile) throws ExcelExporterException{
        exportToFile(t, new File(destFile));
    }
    
    @Override
    public final void exportToFile(Collection<? extends T> t, String destFile) throws ExcelExporterException{
        exportToFile(t, new File(destFile));
    }

    protected String writeHeaders() {
        return EMPTY_STRING;
    }
    
    @Override
    public void exportToFile(Collection<? extends T> objects, File destFile) throws ExcelExporterException{
    
        final Collection<String> lines = convertObjectsToLines(objects);
        try (final PrintWriter pw = new PrintWriter(destFile)) {
            final String header = writeHeaders();
            if(!header.isEmpty()) {
                pw.println(header);
            }
            for (String line : lines) {
                pw.println(line);
            }
        } catch (Exception e) {
        
        }
    }
    
    protected abstract String convertObjectToLine(T t);
    
    protected final Collection<String> convertObjectsToLines(Collection<? extends T> collection){
        return collection.stream()
                .map(this::convertObjectToLine)
                .collect(Collectors.toList());
    }
    
    protected final String valueOf(Object data) {
        return Optional.ofNullable(data)
                .map(String::valueOf)
                .orElse(EMPTY_STRING);
    }
    
}
