package must.belmo.excel.importexport;

import must.belmo.excel.importexport.exception.ExcelExporterException;
import must.belmo.excel.importexport.utils.TypesUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelExporterService<T> {
	private File destinationFile;
	private ColumnsMapper<ExcelExporterService<T>> aColMapper;
	private Collection<T> content;
	private boolean writeHeaders;
	private int initialRow;
	public ExcelExporterService (){
		aColMapper = new ColumnsMapper<>(this);
	}
	
	public ColumnsMapper<ExcelExporterService<T>> map(String columnName) {
		return aColMapper.map(columnName);
	}
	
	public static <R> ExcelExporterService<R> exportContent(Collection<R> content) {
		ExcelExporterService<R> rExcelExporterService = new ExcelExporterService<>();
		rExcelExporterService.content = content;
		return rExcelExporterService;
	}
	
	public ExcelExporterService<T> toFile(File file) {
		this.destinationFile = file;
		return this;
	}
	
	public ExcelExporterService<T> withHeaders() {
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
				final Object valueFromField = TypesUtils.getValueFromField(item, key);
				
				map.put(cell, valueFromField);
			}
			listOfMaps.add(map);
		}
		return listOfMaps;
	}
	
	private <V> void writeMapToExcelFile(List<Map<Integer, V>> listMap, File outputFile) throws ExcelExporterException {
		
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			final Sheet sheet = workbook.createSheet("Sheet 0");
			if (writeHeaders) {
				final List<String> headers = aColMapper.get().keySet()
						.stream()
						.sorted(Comparator.comparing(o -> aColMapper.get().get(o)))
						.collect(Collectors.toList());
				writeHeaders(headers, sheet);
			}
			
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
	
	private void writeHeaders(List<String> headers, Sheet sheet) {
		final Row header = sheet.createRow(0);
		int currentCell = 0;
		for (String key : headers) {
			header.createCell(currentCell).setCellValue(String.valueOf(key));
			currentCell++;
		}
	}
}
