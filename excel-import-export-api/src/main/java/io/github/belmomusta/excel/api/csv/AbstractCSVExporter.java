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
        return String.join(COMMA, getHeaderEntries());
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
            throw new ExcelExporterException("Error while exporting " + getClassName(), e);
        }
    }
    
    protected final String convertObjectToLine(T item) {
        final Collection<String> cells = getEntries(item);
        return cells.stream()
                .map(this::valueOf)
                .collect(Collectors.joining(COMMA));
    }
    protected abstract Collection<String> getEntries(T item);
    protected Collection<String> getHeaderEntries(){
        return Collections.emptyList();
    }
    
    protected final Collection<String> convertObjectsToLines(Collection<? extends T> collection){
        return collection.stream()
                .map(this::convertObjectToLine)
                .collect(Collectors.toList());
    }
    
    protected final String valueOf(Object data) {
        return Optional.ofNullable(data)
                .map(String::valueOf)
                .map(this::escapeSpecialCharacters)
                .orElse(EMPTY_STRING);
    }
    
    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(COMMA) || data.contains(DOUBLE_QUOTES) || data.contains("'")) {
            data = data.replace(DOUBLE_QUOTES, "\"\"");
            escapedData = DOUBLE_QUOTES + data + DOUBLE_QUOTES;
        }
        return escapedData;
    }

}
