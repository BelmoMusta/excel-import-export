package must.belmo.excel.importexport;

import must.belmo.excel.importexport.functional.Extractor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ExcelExporterService<T> {
	
	public void export(Collection<T> content, Map<String, Extractor<T, String>> extractors, File filePath) throws IOException {
		final List<Map<String, String>> maps = convertToMap(content, extractors);
		writeMapToExcelFile(maps, filePath);
	}
	
	private List<Map<String, String>> convertToMap(Collection<T> content,
												   Map<String, Extractor<T, String>> extractors) {
		final List<Map<String, String>> listOfMaps = new ArrayList<>();
		
		for (T item : content) {
			final Map<String, String> map = new LinkedHashMap<>();
			
			for (Map.Entry<String, Extractor<T, String>> entry : extractors.entrySet()) {
				final Extractor<T, String> extractor = entry.getValue();
				final String key = entry.getKey();
				final String value = extractor.extract(item);
				map.put(key, value);
			}
			
			listOfMaps.add(map);
		}
		return listOfMaps;
	}
	
	private <K, V> void writeMapToExcelFile(List<Map<K, V>> listMap, File outputFile) throws IOException {
		
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			final Sheet sheet = workbook.createSheet("Sheet 0");
			writeHeaders(listMap, sheet);
			
			int currentRow = 1;
			for (Map<K, V> map : listMap) {
				final Row row = sheet.createRow(currentRow++);
				createRowFromMap(map, row);
			}
			
			writeWorkbookToFile(workbook, outputFile);
		}
	}
	
	private void writeWorkbookToFile(XSSFWorkbook workbook, File outputFile) throws IOException {
		final FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
		workbook.write(fileOutputStream);
		workbook.close();
	}
	
	private <K, V> void createRowFromMap(Map<K, V> map, Row row) {
		int currentCell = 0;
		for (Map.Entry<K, V> keyValue : map.entrySet()) {
			row.createCell(currentCell).setCellValue(String.valueOf(keyValue.getValue()));
			currentCell++;
		}
	}
	
	private <K, V> void writeHeaders(List<Map<K, V>> listMap, Sheet sheet) {
		final Row header = sheet.createRow(0);
		final Set<K> keySet = listMap.stream()
				.flatMap(map -> map.keySet().stream())
				.collect(Collectors.toCollection(LinkedHashSet::new));
		int currentCell = 0;
		for (K key : keySet) {
			header.createCell(currentCell).setCellValue(String.valueOf(key));
			currentCell++;
		}
	}
}
