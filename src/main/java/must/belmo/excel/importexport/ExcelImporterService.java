package must.belmo.excel.importexport;

import must.belmo.excel.importexport.exception.ExcelImporterAccessException;
import must.belmo.excel.importexport.exception.ExcelImporterDefaultConstructorException;
import must.belmo.excel.importexport.exception.ExcelImporterException;
import must.belmo.excel.importexport.exception.ExcelImporterNoFieldException;
import must.belmo.excel.importexport.utils.CellUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("all")
public class ExcelImporterService<T> {
	public Collection<T> importFromExcelFile(Class<T> cls, String excelFile, int sheetNumber, Map<String,
			Integer> map) throws ExcelImporterException {
		return importFromExcelFile(cls, new File(excelFile), sheetNumber, map);
	}
	
	public Collection<T> importFromExcelFile(Class<T> cls, File excelFilePath, int sheetNumber, Map<String,
			Integer> map) throws ExcelImporterException {
		final Workbook workbook;
		try {
			workbook = new XSSFWorkbook(new FileInputStream(excelFilePath));
		} catch (IOException e) {
			throw new ExcelImporterException(e);
		}
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
		final Constructor<T> constructor;
		try {
			constructor = cls.getConstructor();
		} catch (NoSuchMethodException e) {
			throw new ExcelImporterDefaultConstructorException(cls);
		}
		final T instance;
		try {
			instance = constructor.newInstance();
		} catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
			throw new ExcelImporterException("Cannot access the class " + cls.getCanonicalName());
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
		try {
			field = cls.getDeclaredField(toFieldName);
			field.setAccessible(true);
		} catch (NoSuchFieldException e) {
			throw new ExcelImporterNoFieldException(cls, toFieldName);
		}
		try {
			field.set(target, CellUtils.getCellValue(row, fromCellNumber, field.getType()));
		} catch (IllegalAccessException e) {
			throw new ExcelImporterAccessException(cls, toFieldName);
		}
	}
}
