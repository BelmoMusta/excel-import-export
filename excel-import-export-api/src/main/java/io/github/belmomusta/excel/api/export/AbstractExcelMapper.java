package io.github.belmomusta.excel.api.export;

import io.github.belmomusta.excel.api.ExcelExporter;
import io.github.belmomusta.excel.api.exception.ExcelExporterException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public abstract class AbstractExcelMapper<T> implements ExcelExporter<T> {
    protected void writeWorkbookToFile(XSSFWorkbook workbook, File outputFile) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        workbook.write(fileOutputStream);
        workbook.close();
    }
    
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

    protected void writeHeaders(Sheet sheet) {
    }
    
    protected final String valueOf(Object data) {
        return Optional.ofNullable(data)
                .map(String::valueOf)
                .orElse("");
    }
   
    protected final void addCellWithValue(Row row, Object data) {
        short lastCellNum = row.getLastCellNum();
        if (lastCellNum == -1) {
            lastCellNum = 0;
        }
        addCellWithValue(row, lastCellNum, data);
    }
    
    protected final void addCellWithValue(Row row, int rowNum, Object data) {
        row.createCell(rowNum).setCellValue(valueOf(data));
    }

}
