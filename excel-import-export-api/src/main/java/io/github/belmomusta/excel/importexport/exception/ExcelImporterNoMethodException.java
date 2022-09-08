package io.github.belmomusta.excel.importexport.exception;

public class ExcelImporterNoMethodException extends ExcelImporterException {
	public ExcelImporterNoMethodException(Class<?> cls, String methodName) {
		super("No method with the name '" + methodName + "' is found for the class " + cls.getCanonicalName());
		
	}
}
