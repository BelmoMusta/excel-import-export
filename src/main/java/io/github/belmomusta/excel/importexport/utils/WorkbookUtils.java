package io.github.belmomusta.excel.importexport.utils;

import io.github.belmomusta.excel.importexport.exception.ExcelImporterException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WorkbookUtils {
	public static Workbook getWorkbookFromFile(File excelFilePath) throws ExcelImporterException {
		final Workbook workbook;
		try {
			workbook = new XSSFWorkbook(new FileInputStream(excelFilePath));
		} catch (IOException e) {
			throw new ExcelImporterException(e);
		}
		return workbook;
	}
}
