package io.github.belmomusta.excel.importexport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

public abstract class AbstractExcelMapper<T> implements ExcelMapper<T> {
    protected void writeWorkbookToFile(XSSFWorkbook workbook, File outputFile) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        workbook.write(fileOutputStream);
        workbook.close();
    }

    protected void writeHeaders(Sheet sheet) {
    }
    
    protected String valueOf(Object data) {
        return Optional.ofNullable(data)
                .map(String::valueOf)
                .orElse("");
    }
    protected Cell addCell(Row header) {
        short lastCellNum = header.getLastCellNum();
        if(lastCellNum == -1){
            lastCellNum = 0;
        }
        return header.createCell(lastCellNum);
    }

}
