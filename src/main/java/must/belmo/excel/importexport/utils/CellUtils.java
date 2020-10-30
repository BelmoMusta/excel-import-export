package must.belmo.excel.importexport.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.Date;
import java.util.Optional;

public class CellUtils {
	public static <T> T getCellCorrectNumericValue(Cell cell, Class<T> targetType) {
		final Object returnValue;
		if (cell == null) {
			if (targetType.isPrimitive()) {
				returnValue = TypesUtils.getDefaultValue(targetType);
			} else {
				returnValue = null;
			}
		} else {
			final double d = cell.getNumericCellValue();
			if (targetType == Integer.class || targetType == int.class) {
				returnValue = (int) d;
			} else {
				returnValue = d;
			}
		}
		return (T) returnValue;
	}
	
	public static <T> T mapCellToTheCorrectType(Cell cell, Class<T> cls) {
		Object returnValue = null;
		Class targetType = TypesUtils.wrap(cls);
		if (targetType == String.class) {
			returnValue = cell.getStringCellValue();
		} else if (Number.class.isAssignableFrom(targetType)) {
			returnValue = getCellCorrectNumericValue(cell, cls);
		} else if (Date.class.isAssignableFrom(targetType)) {
			returnValue = cell.getDateCellValue();
		}
		return (T) returnValue;
	}
	
	public static <T> T getCellValue(Row row, int i, Class<T> targetType) {
		return Optional.ofNullable(row.getCell(i))
				.map(cell -> mapCellToTheCorrectType(cell, targetType))
				.orElse(null);
	}
}
