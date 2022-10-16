package io.github.belmomusta.exporter.api.utils;

import io.github.belmomusta.exporter.api.annotation.Export;
import io.github.belmomusta.exporter.api.common.ExportType;

import java.util.Arrays;

public class AnnotationsUtils {
	public static boolean isExcel(Export export){
		return export != null
				&& Arrays.asList(export.type()).contains(ExportType.EXCEL);
	}
	
	public static boolean isCSV(Export export){
		return export != null
				&& Arrays.asList(export.type()).contains(ExportType.CSV);
	}
}
