package io.github.belmomusta.excel.importexport;

import io.github.belmomusta.excel.importexport.exception.ExcelExporterException;
import io.github.belmomusta.excel.importexport.utils.TypesUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelExporter<T> {
	private File destinationFile;
	protected ColumnsMapper<ExcelExporter<T>> aColMapper;
	protected Collection<T> content;
	private boolean writeHeaders;
	private int initialRow;
	private Collection<String> headers = new ArrayList<>();
	
	public ExcelExporter() {
		aColMapper = new ColumnsMapper<>(this);
	}
	
	public ColumnsMapper<ExcelExporter<T>> map(String columnName) {
		return aColMapper.map(columnName);
	}
	public ColumnsMapper<ExcelExporter<T>> mapMethod(String columnName) {
		return aColMapper.map("#"+columnName);
	}
	
	public static <R> ExcelExporter<R> exportContent(Collection<R> content) {
		ExcelExporter<R> rExcelExporter = new ExcelExporter<>();
		rExcelExporter.content = content;
		return rExcelExporter;
	}
	
	public ExcelExporter<T> toFile(File file) {
		this.destinationFile = file;
		return this;
	}
	
	public ExcelExporter<T> withHeaders(String... headers) {
		this.headers.addAll(Arrays.asList(headers));
		this.writeHeaders = true;
		initialRow = 1;
		return this;
	}
	
	public void export() throws ExcelExporterException {
		final List<Map<Integer, Object>> maps = convertToMap(content, aColMapper.get());
		writeMapToExcelFile(maps, destinationFile);
	}
	
	private List<Map<Integer, Object>> convertToMap(Collection<T> content,
													Map<String, Integer> extractors) throws ExcelExporterException {
		final List<Map<Integer, Object>> listOfMaps = new ArrayList<>();
		
		for (T item : content) {
			final Map<Integer, Object> map = new LinkedHashMap<>();
			
			for (Map.Entry<String, Integer> entry : extractors.entrySet()) {
				final Integer cell = entry.getValue();
				final String key = entry.getKey();
				final Object valueFromField;
				if (key.startsWith("#")) {
					valueFromField = TypesUtils.getValueFromMethod(item, key.substring(1));
				} else {
					valueFromField = TypesUtils.getValueFromField(item, key);
				}
				
				map.put(cell, valueFromField);
			}
			listOfMaps.add(map);
		}
		return listOfMaps;
	}
	
	private <V> void writeMapToExcelFile(List<Map<Integer, V>> listMap, File outputFile) throws ExcelExporterException {
		
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			final Sheet sheet = workbook.createSheet("Sheet 0");
			writeHeaders(sheet);
			int currentRow = initialRow;
			for (Map<Integer, V> map : listMap) {
				final Row row = sheet.createRow(currentRow++);
				createRowFromMap(map, row);
			}
			
			writeWorkbookToFile(workbook, outputFile);
		} catch (IOException e) {
			throw new ExcelExporterException("Error writing to file ", e);
		}
	}
	
	private void writeHeaders(Sheet sheet) {
		if (writeHeaders) {
			final Collection<String> headersToWrite;
			if (headers.isEmpty()) {
				headersToWrite = aColMapper.get().keySet()
						.stream()
						.sorted(Comparator.comparing(o -> aColMapper.get().get(o)))
						.collect(Collectors.toList());
			} else {
				headersToWrite = this.headers;
			}
			final Row header = sheet.createRow(0);
			int currentCell = 0;
			for (String key : headersToWrite) {
				header.createCell(currentCell).setCellValue(String.valueOf(key));
				currentCell++;
			}
		}
	}
	
	private void writeWorkbookToFile(XSSFWorkbook workbook, File outputFile) throws IOException {
		final FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
		workbook.write(fileOutputStream);
		workbook.close();
	}
	
	private <V> void createRowFromMap(Map<Integer, V> map, Row row) {
		for (Map.Entry<Integer, V> keyValue : map.entrySet()) {
			final Integer cell = keyValue.getKey();
			row.createCell(cell).setCellValue(String.valueOf(keyValue.getValue()));
		}
	}
}
