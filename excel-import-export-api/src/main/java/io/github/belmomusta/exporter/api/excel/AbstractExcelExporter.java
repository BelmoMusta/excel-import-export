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

public abstract class AbstractExcelExporter<T> extends AbstractExporter<T> {
    protected final void writeWorkbookToFile(XSSFWorkbook workbook, File outputFile) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        workbook.write(fileOutputStream);
        workbook.close();
    }
    
    protected abstract void createRows(Sheet sheet, T object);
    
    @Override
    public void exportToFile(Collection<? extends T> objects, File destFile) throws ExporterException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            final Sheet sheet = workbook.createSheet(getSimpleClassName());
            writeHeaders(sheet);
            for (T object : objects) {
                createRows(sheet, object);
            }
            writeWorkbookToFile(workbook, destFile);
        } catch (Exception e) {
            throw new ExporterException(getClassName(), e);
        }
    }
    
    protected void writeHeaders(Sheet sheet) {
    }
    
    protected final void addCellWithValue(Row row, Object object) {
        short lastCellNum = row.getLastCellNum();
        if (lastCellNum == -1) {
            lastCellNum = 0;
        }
        addCellWithValue(row, lastCellNum, object);
    }
    
    protected final void addCellWithValue(Row row, int rowNum, Object object) {
        row.createCell(rowNum).setCellValue(valueOf(object));
    }

}
