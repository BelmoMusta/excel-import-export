package io.github.belmomusta.exporter.api.csv;

import io.github.belmomusta.exporter.api.exception.ExporterException;
import io.github.belmomusta.exporter.api.formatter.Formatter;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractCSVExporter<T> implements CSVExporter<T> {
    
    protected abstract Collection<String> getEntries(T object);
    
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

    protected String writeHeaders() {
        return String.join(COMMA, getHeaderEntries());
    }
    
    @Override
    public void exportToFile(Collection<? extends T> objects, File destFile) throws ExporterException {
        
        try (final PrintWriter printWriter = new PrintWriter(destFile)) {
            final Collection<String> lines = convertObjectsToLines(objects);
            final String header = writeHeaders();
            if(!header.isEmpty()) {
                printWriter.println(header);
            }
            for (String line : lines) {
                printWriter.println(line);
            }
        } catch (Exception exception) {
            throw new ExporterException(getClassClass().getName(), exception);
        }
    }
    
    protected final String convertObjectToLine(T object) {
        final Collection<String> cells = getEntries(object);
        return cells.stream()
                .map(this::valueOf)
                .collect(Collectors.joining(COMMA));
    }
    
    protected Collection<String> getHeaderEntries(){
        return Collections.emptyList();
    }
    
    protected final String valueOf(Object object) {
        return Optional.ofNullable(object)
                .map(String::valueOf)
                .map(this::escapeSpecialCharacters)
                .orElse(EMPTY_STRING);
    }
    
    protected final<X> String valueOf(X object, Formatter<X> formatter) {
        return Optional.ofNullable(object)
                .map(formatter::format)
                .map(this::escapeSpecialCharacters)
                .orElse(EMPTY_STRING);
    }
    
    private Collection<String> convertObjectsToLines(Collection<? extends T> objects){
        return objects.stream()
                .map(this::convertObjectToLine)
                .collect(Collectors.toList());
    }
    
    private String escapeSpecialCharacters(String object) {
        String escapedData = object.replaceAll(LINE_BREAK, SPACE);
        if (object.contains(COMMA)
                || object.contains(DOUBLE_QUOTES)
                || object.contains(SINGLE_QUOTE)) {
            object = object.replace(DOUBLE_QUOTES, ESCAPED_DOUBLE_QUOTES);
            escapedData = DOUBLE_QUOTES + object + DOUBLE_QUOTES;
        }
        return escapedData;
    }

}
