package must.belmo.excel.importexport;

import must.belmo.excel.importexport.exception.ExcelImporterException;

import java.io.File;
import java.util.Collection;

public class ExcelImporterAnnotationService<T> extends ExcelImporterService<T> {
	public Collection<T> importFromExcelFile(Class<T> cls, String excelFile, int sheetNumber) throws ExcelImporterException {
		return importFromExcelFile(cls, new File(excelFile), sheetNumber);
	}
	
	public Collection<T> importFromExcelFile(Class<T> cls, File excelFilePath, int sheetNumber) throws ExcelImporterException {
		return super.importFromExcelFile(cls, excelFilePath, sheetNumber, null);
	}
	
}
