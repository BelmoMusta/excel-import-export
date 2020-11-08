package io.github.belmomusta.excel.importexport;

import io.github.belmomusta.excel.importexport.annotation.ExcelColumn;

import java.lang.reflect.Field;

public class ExcelImporterAnnotation<T> extends ExcelImporter<T> {
	
	private ExcelImporterAnnotation(Class<T> aClass) {
		super(aClass);
		mapAnnotatedFields(aClass);
	}
	
	public static <R> ExcelImporterAnnotation<R> extract(Class<R> aClass) {
		return new ExcelImporterAnnotation<>(aClass);
	}
	
	
	private void mapAnnotatedFields(Class<T> target) {
		
		final Field[] declaredFields = target.getDeclaredFields();
		for (Field declaredField : declaredFields) {
			final ExcelColumn excelColumn = declaredField.getAnnotation(ExcelColumn.class);
			if (excelColumn != null) {
				map(declaredField.getName()).toCell(excelColumn.value());
			}
		}
	}
}
