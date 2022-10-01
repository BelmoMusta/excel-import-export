package io.github.belmomusta.exporter.api.exception;

public class ExporterException extends Exception {
	
	public ExporterException(String className, Throwable e) {
		super("Error while exporting '" + className + "'", e);
	}
}
