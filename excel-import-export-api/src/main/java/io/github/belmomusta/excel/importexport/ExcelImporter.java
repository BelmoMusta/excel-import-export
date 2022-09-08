package io.github.belmomusta.excel.importexport;

import io.github.belmomusta.excel.importexport.exception.ExcelImporterAccessException;
import io.github.belmomusta.excel.importexport.exception.ExcelImporterException;
import io.github.belmomusta.excel.importexport.exception.ExcelImporterNoFieldException;
import io.github.belmomusta.excel.importexport.exception.ExcelImporterNoMethodException;
import io.github.belmomusta.excel.importexport.utils.CellUtils;
import io.github.belmomusta.excel.importexport.utils.TypesUtils;
import io.github.belmomusta.excel.importexport.utils.WorkbookUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ExcelImporter<T> extends AbstractMapper {
	
	protected Class<T> cls;
	private File file;
	protected final ColumnsMapper<ExcelImporter<T>> columnsMapper;
	private int sheetNumber;
	private boolean sheetNumberSpecified;
	private Collection<T> items;
	private boolean collectionTypeSpecified;
	private int[] rowNumbers = {};
	private boolean rowNumberSpecified;
	
	protected ExcelImporter(Class<T> aClass) {
		this.cls = aClass;
		this.columnsMapper = new ColumnsMapper<>(this);
		items = new ArrayList<>(); // default
	}
	
	public static <R> ExcelImporter<R> extract(Class<R> aClass) {
		return new ExcelImporter<>(aClass);
	}
	
	public ExcelImporter<T> from(File file) {
		this.file = file;
		return this;
	}
	
	public ExcelImporter<T> from(String file) {
		return from(new File(file));
	}
	
	public ExcelImporter<T> fromAllSheets() {
		sheetNumberSpecified = false;
		return this;
	}
	
	public ExcelImporter<T> toCollection(Collection<T> aCollection) throws ExcelImporterException {
		items = Optional.ofNullable(aCollection)
				.orElseThrow(() -> new ExcelImporterException("The provided collection is null"));
		collectionTypeSpecified = true;
		return this;
	}
	
	public ExcelImporter<T> inSheetNumber(int sheetNumber) {
		this.sheetNumber = sheetNumber;
		sheetNumberSpecified = true;
		return this;
	}
	
	public ExcelImporter<T> itemsAtRows(int... rowNumbers) {
		// access to a row in O(1)
		this.rowNumbers = rowNumbers;
		rowNumberSpecified = true;
		return this;
	}
	
	public ColumnsMapper<ExcelImporter<T>> map(String columnName) {
		return columnsMapper.map(columnName);
	}
	
	public Collection<T> get() throws ExcelImporterException {
		final Workbook workbook = WorkbookUtils.getWorkbookFromFile(file);
		final List<Sheet> sheetList = getSheets(workbook);
		final List<Row> rows = getRows(sheetList);
		final Collection<T> innerItems = convertRowsToItems(rows);
		
		if (collectionTypeSpecified) {
			items.addAll(innerItems);
		} else {
			items = innerItems;
		}
		return items;
	}
	
	public Collection<T> convertRowsToItems(List<Row> rows) throws ExcelImporterException {
		final Collection<T> innerItems = new ArrayList<>();
		for (Row row : rows) {
			T object = convertRowToObject(row, cls);
			innerItems.add(object);
		}
		return innerItems;
	}
	
	public List<Sheet> getSheets(Workbook workbook) {
		final List<Sheet> sheetList = new ArrayList<>();
		if (sheetNumberSpecified) {
			sheetList.add(workbook.getSheetAt(this.sheetNumber));
		} else {
			final Iterator<Sheet> sheetIterator = workbook.sheetIterator();
			while (sheetIterator.hasNext()) {
				sheetList.add(sheetIterator.next());
			}
		}
		return sheetList;
	}
	
	public List<Row> getRows(List<Sheet> sheetList) {
		final List<Row> rows = new ArrayList<>();
		for (Sheet sheet : sheetList) {
			if (rowNumberSpecified) {
				for (int i : rowNumbers) {
					rows.add(sheet.getRow(i));
				}
			} else {
				final Iterator<Row> rowIterator = sheet.rowIterator();
				while (rowIterator.hasNext()) {
					rows.add(rowIterator.next());
				}
			}
		}
		return rows;
	}
	
	private T convertRowToObject(Row row, Class<T> cls) throws ExcelImporterException {
		final T instance = TypesUtils.createInstanceUsingDefaultConstructor(cls);
		for (Map.Entry<String, Integer> entry : columnsMapper.columnsMappers.entrySet()) {
			final String fieldName = entry.getKey();
			final Integer cellNumber = entry.getValue();
			if(fieldName.startsWith("#")) {
                extractFromRowUsingMethod(row, cls, instance, fieldName, cellNumber);
            } else {
                extractFromRow(row, cls, instance, fieldName, cellNumber);
            }
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

	private void extractFromRowUsingMethod(Row row, Class<T> cls, T target, String methodName, Integer fromCellNumber) throws ExcelImporterException {
		final Method method;
		final Method setter;
		Object cellValue;
		try {
			method = cls.getDeclaredMethod(methodName.substring(1)); // this is a getter for now
			setter = cls.getDeclaredMethod(methodName.substring(1).replaceFirst("get","set"), method.getReturnType());
			method.setAccessible(true);
			cellValue = CellUtils.getCellValue(row, fromCellNumber, method.getReturnType());
		} catch (NoSuchMethodException e) {
			throw new ExcelImporterNoMethodException(cls, methodName);
		}
		try {
            setter.invoke(target, cellValue);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new ExcelImporterAccessException(cls, methodName);
		}
	}
}
