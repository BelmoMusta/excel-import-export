package io.github.belmomusta.excel.importexport;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class AbstractExcelMapper<T> implements ExcelMapper<T> {
    protected void writeWorkbookToFile(XSSFWorkbook workbook, File outputFile) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        workbook.write(fileOutputStream);
        workbook.close();
    }

    protected void writeHeaders(Sheet sheet) {
    }

}
