package io.github.belmomusta.exporter.api.csv;

import io.github.belmomusta.exporter.api.common.AbstractExporter;
import io.github.belmomusta.exporter.api.exception.ExporterException;
import io.github.belmomusta.exporter.api.formatter.Formatter;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractCSVExporter<T> extends AbstractExporter<T> implements CSVExporter<T> {
    
    protected String writeHeaders() {
        return String.join(COMMA, getHeadersEntries());
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
            throw new ExporterException(getClassName().getName(), exception);
        }
    }
    
    protected final String convertObjectToLine(T object) {
        final Collection<String> cells = getRowEntries(object);
        return cells.stream()
                .map(this::valueOf)
                .collect(Collectors.joining(COMMA));
    }
    
    @Override
    public final <X> String valueOf(X object) {
        return Optional.ofNullable(object)
                .map(String::valueOf)
                .map(this::escapeSpecialCharacters)
                .orElse(EMPTY_STRING);
    }
    
    @Override
    public final<X> String valueOf(X object, Formatter<X> formatter) {
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
