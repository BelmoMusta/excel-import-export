package io.github.belmomusta.excel.importexport.exception;

public class ExcelImporterAccessException extends ExcelImporterException {
	public ExcelImporterAccessException(Class<?> cls, String fieldName) {
		super("Cannot access field with the name '" + fieldName + "' on the class " + cls.getCanonicalName());
		
		
	}
}
