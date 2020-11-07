package io.github.belmomusta.excel.importexport;

import io.github.belmomusta.excel.importexport.annotation.ExcelCell;

import java.lang.reflect.Field;

public class ExcelImporterAnnotation<T> extends ExcelImporter<T> {
	
	private ExcelImporterAnnotation(Class<T> aClass) {
		super(aClass);
		createColmnMappersFromAnnotation(aClass);
	}
	
	public static <R> ExcelImporterAnnotation<R> extract(Class<R> aClass) {
		return new ExcelImporterAnnotation<>(aClass);
	}
	
	
	private void createColmnMappersFromAnnotation(Class<T> target) {
		
		final Field[] declaredFields = target.getDeclaredFields();
		for (Field declaredField : declaredFields) {
			final ExcelCell excelCellAnnotation = declaredField.getAnnotation(ExcelCell.class);
			if (excelCellAnnotation != null) {
				super.columnsMapper.map(declaredField.getName()).toCell(excelCellAnnotation.value());
			}
		}
	}
}
