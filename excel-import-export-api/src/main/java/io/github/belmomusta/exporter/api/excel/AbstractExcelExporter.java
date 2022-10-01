package io.github.belmomusta.exporter.api.excel;

import io.github.belmomusta.exporter.api.common.AbstractExporter;
import io.github.belmomusta.exporter.api.exception.ExporterException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractExcelExporter<T> extends AbstractExporter<T> {
    protected final void writeWorkbookToFile(XSSFWorkbook workbook, File outputFile) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        workbook.write(fileOutputStream);
        workbook.close();
    }
    
    protected final void createRows(Sheet sheet, T object){
        final Row aRow = sheet.createRow(sheet.getLastRowNum() + 1);
        convertObjectToRow(object, aRow);
    }
    
    protected void convertObjectToRow(T object, Row aRow){
        final List<String> entries = getRowEntries(object);
        for (String entry : entries) {
            addCellWithValue(aRow, entry);
        }
    }
    
    protected List<String> getHeadersEntries(){
        return Collections.emptyList();
    }
    
    protected abstract List<String> getRowEntries(T object);
    
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
    
    protected void writeHeaders(Sheet sheet) {
        final Row header = sheet.createRow(0);
        final List<String> headerEntries = getHeadersEntries();
    
        for (String headerEntry : headerEntries) {
            addCellWithValue(header, headerEntry);
        }
    }
    
    protected final void addCellWithValue(Row row, Object object) {
        short lastCellNum = row.getLastCellNum();
        if (lastCellNum == -1) {
            lastCellNum = 0;
        }
        addCellWithValue(row, lastCellNum, object);
    }
    
    private void addCellWithValue(Row row, int rowNum, Object object) {
        row.createCell(rowNum).setCellValue(valueOf(object));
    }

}
