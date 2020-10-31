package must.belmo.excel.importexport;

import must.belmo.excel.importexport.annotation.ExcelCell;
import must.belmo.excel.importexport.exception.ExcelImporterAccessException;
import must.belmo.excel.importexport.exception.ExcelImporterException;
import must.belmo.excel.importexport.exception.ExcelImporterNoFieldException;
import must.belmo.excel.importexport.utils.CellUtils;
import must.belmo.excel.importexport.utils.TypesUtils;
import must.belmo.excel.importexport.utils.WorkbookUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//@SuppressWarnings("all")
public class ExcelImporterService<T> {
	public Collection<T> importFromExcelFile(Class<T> cls, String excelFile, int sheetNumber, Map<String,
			Integer> map) throws ExcelImporterException {
		return importFromExcelFile(cls, new File(excelFile), sheetNumber, map);
	}
	
	public Collection<T> importFromExcelFile(Class<T> cls, File excelFilePath, int sheetNumber, Map<String,
			Integer> map) throws ExcelImporterException {
		final Workbook workbook = WorkbookUtils.getWorkbookFromFile(excelFilePath);
		final Sheet sheet = workbook.getSheetAt(sheetNumber);
		final Iterator<Row> rowIterator = sheet.rowIterator();
		final Collection<T> items = new ArrayList<>();
		
		while (rowIterator.hasNext()) {
			T object = convertRowToObject(rowIterator.next(), map, cls);
			items.add(object);
		}
		return items;
	}
	
	private T convertRowToObject(Row row, Map<String, Integer> rowMapper, Class<T> cls) throws ExcelImporterException {
		final T instance = TypesUtils.createInstanceUsingDefaultConstructor(cls);
		
		if(rowMapper == null) {
			rowMapper = createRowMappersFromAnnotation(cls);
		}
		for (Map.Entry<String, Integer> entry : rowMapper.entrySet()) {
			final String fieldName = entry.getKey();
			final Integer cellNumber = entry.getValue();
			extractFromRow(row, cls, instance, fieldName, cellNumber);
		}
		return instance;
	}
	
	
	private void extractFromRow(Row row, Class<T> cls, T target, String toFieldName, Integer fromCellNumber) throws ExcelImporterException {
		final Field field;
		Object cellValue;
		try {
			field = cls.getDeclaredField(toFieldName);
			field.setAccessible(true);
			cellValue = CellUtils.getCellValue(row, fromCellNumber, field.getType());
		} catch (NoSuchFieldException e) {
			throw new ExcelImporterNoFieldException(cls, toFieldName);
		}
		try {
			field.set(target, cellValue);
		} catch (IllegalAccessException e) {
			throw new ExcelImporterAccessException(cls, toFieldName);
		}
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
