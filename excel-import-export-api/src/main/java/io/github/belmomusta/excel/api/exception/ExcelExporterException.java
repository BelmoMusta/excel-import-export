package io.github.belmomusta.excel.api.exception;

public class ExcelExporterException extends Exception {
	public ExcelExporterException(String s) {
		super(s);
	}
	
	public ExcelExporterException(String message, Throwable e) {
		super(message, e);
	}
}
