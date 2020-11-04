package must.belmo.excel.importexport;

import java.util.HashMap;
import java.util.Map;

public class ColumnsMapper<S> {
	public final Map<String, Integer> columnsMappers;
	private S service;
	private String currentColumnName;
	
	public ColumnsMapper(S service) {
		this.service = service;
		columnsMappers = new HashMap<>();
	}
	public ColumnsMapper<S> map(String columnName) {
		currentColumnName = columnName;
		return this;
	}
	public S toCell(Integer columnName) {
		columnsMappers.put(currentColumnName, columnName);
		return service;
	}
	public Map<String, Integer> get() {
		return columnsMappers;
	}
}
