package io.github.belmomusta.exporter.processor;

public enum ExportType {
	CSV(".export.csv.", "CSVExporter"),
	EXCEL(".export.excel.", "ExcelExporter"),
	NONE("", ""),
	
	;
	
	final String packagePrefix;
	final String classSuffix;
	
	ExportType(String packagePrefix, String classSuffix) {this.packagePrefix = packagePrefix;
		this.classSuffix = classSuffix;
	}
	
	public String getPackagePrefix() {
		return packagePrefix;
	}
	
	public String getClassSuffix() {
		return classSuffix;
	}
}
