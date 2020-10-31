package must.belmo.excel.importexport;

import must.belmo.excel.importexport.annotation.ExcelCell;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ExcelImporterAnnotation<T> extends ExcelImporter<T> {
	
	private ExcelImporterAnnotation(Class<T> aClass) {
		super(aClass);
		super.withColumnsMapper(createRowMappersFromAnnotation(aClass));
	}
	
	public static <R> ExcelImporterAnnotation<R> extract(Class<R> aClass) {
		return new ExcelImporterAnnotation<>(aClass);
	}
	
	@Override
	public ExcelImporterAnnotation<T> withColumnsMapper(Map<String, Integer> mapper) {
		// do not alter the mappers because they will come from the annotations
		return this;
	}
	
	private Map<String, Integer> createRowMappersFromAnnotation(Class<T> target) {
		final Map<String, Integer> map = new HashMap<>();
		final Field[] declaredFields = target.getDeclaredFields();
		for (Field declaredField : declaredFields) {
			final ExcelCell excelCellAnnotation = declaredField.getAnnotation(ExcelCell.class);
			if (excelCellAnnotation != null) {
				map.put(declaredField.getName(), excelCellAnnotation.value());
			}
		}
		return map;
	}
}
