package io.github.belmomusta.excel.importexport.exception;

public class ExcelImporterNoFieldException extends ExcelImporterException {
	public ExcelImporterNoFieldException(Class<?> cls, String fieldName) {
		super("No field with the name '" + fieldName + "' is found for the class " + cls.getCanonicalName());
		
	}
}
