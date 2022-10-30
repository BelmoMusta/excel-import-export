package io.github.belmomusta.exporter.api.excel;

import io.github.belmomusta.exporter.api.common.AbstractExporter;
import io.github.belmomusta.exporter.api.exception.ExporterException;
import io.github.belmomusta.exporter.api.formatter.Formatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class AbstractExcelExporter<T> extends AbstractExporter<T> implements ExcelExporter<T>{
    
    @Override
    public void exportToFile(Collection<? extends T> objects, File destFile) throws ExporterException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            final Sheet sheet = workbook.createSheet(getClassName().getSimpleName());
            writeHeaders(sheet);
			
            for (T object : objects) {
                createRows(sheet, object);
            }
            writeWorkbookToFile(workbook, destFile);
        } catch (Exception e) {
            throw new ExporterException(getClassName().getName(), e);
        }
    }
    
    @Override
    public final <X> String valueOf(X object) {
        return Optional.ofNullable(object)
                .map(String::valueOf)
                .orElse(EMPTY_STRING);
    }
    
    @Override
    public final<X> String valueOf(X object, Formatter<X> formatter) {
        return Optional.ofNullable(object)
                .map(formatter::format)
                .orElse(EMPTY_STRING);
    }
    
    private void writeHeaders(Sheet sheet) {
        final List<String> headerEntries = getHeadersEntries();
        if (!headerEntries.isEmpty()) {
            final Row header = sheet.createRow(0);
        
            for (String headerEntry : headerEntries) {
                addCellWithValue(header, headerEntry);
            }
        }
    }
    
    private <X> void addCellWithValue(Row row, X object) {
        short lastCellNum = row.getLastCellNum();
        if (lastCellNum == -1) {
            lastCellNum = 0;
        }
        addCellWithValue(row, lastCellNum, object);
    }
    
    private <X>void addCellWithValue(Row row, int rowNum, X object) {
        row.createCell(rowNum).setCellValue(valueOf(object));
    }
    private void writeWorkbookToFile(XSSFWorkbook workbook, File outputFile) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        workbook.write(fileOutputStream);
        workbook.close();
    }
    
    private void createRows(Sheet sheet, T object){
        final Row aRow = sheet.createRow(sheet.getLastRowNum() + 1);
        convertObjectToRow(object, aRow);
    }
    
    private void convertObjectToRow(T object, Row aRow){
        final List<String> entries = getRowEntries(object);
        for (String entry : entries) {
            addCellWithValue(aRow, entry);
        }
    }
}
