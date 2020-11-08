package io.github.belmomusta.excel.importexport;

import io.github.belmomusta.excel.importexport.annotation.ExcelColumn;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

public class ExcelExporterAnnotation<T> extends ExcelExporter<T> {
	
	public static <R> ExcelExporterAnnotation<R> exportContent(Collection<R> content) {
		ExcelExporterAnnotation<R> rExcelExporterService = new ExcelExporterAnnotation<>();
		rExcelExporterService.content = content;
		if (!content.isEmpty()) {
			R next = content.iterator().next();
			rExcelExporterService.mapAnnotatedFields(next.getClass());
			rExcelExporterService.mapAnnotatedMethods(next.getClass());
		}
		return rExcelExporterService;
	}
	
	private void mapAnnotatedFields(Class target) {
		final Method[] declaredMethods = target.getDeclaredMethods();
		for (Method declaredMethod : declaredMethods) {
			final ExcelColumn excelColumnAnnotation = declaredMethod.getAnnotation(ExcelColumn.class);
			if (excelColumnAnnotation != null) {
				map("#" + declaredMethod.getName()).toCell(excelColumnAnnotation.value());
			}
		}
	}
	
	private void mapAnnotatedMethods(Class target) {
		final Field[] declaredFields = target.getDeclaredFields();
		for (Field declaredField : declaredFields) {
			final ExcelColumn excelColumn = declaredField.getAnnotation(ExcelColumn.class);
			if (excelColumn != null) {
				map(declaredField.getName()).toCell(excelColumn.value());
			}
		}
	}
}
