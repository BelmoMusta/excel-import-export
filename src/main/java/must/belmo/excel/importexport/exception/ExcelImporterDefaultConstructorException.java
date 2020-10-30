package must.belmo.excel.importexport.exception;

public class ExcelImporterDefaultConstructorException extends ExcelImporterException {
	public ExcelImporterDefaultConstructorException(Class<?> cls) {
		super("No default constructor is found for the class " + cls.getCanonicalName());
		
	}
}
