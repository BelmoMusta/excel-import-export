package io.github.belmomusta.excel.importexport;

import java.util.Map;

public class ColumnsMapper<S> {
	public final ColumnsCellsMap columnsMappers;
	private S service;

	public ColumnsMapper(S service) {
		this.service = service;
		columnsMappers = new ColumnsCellsMap();
	}
	public ColumnsMapper<S> map(String columnName) {
	    columnsMappers.map(columnName);
		return this;
	}
	public S toCell(Integer cellNumber) {
		columnsMappers.toCell(cellNumber);
		return service;
	}
	public Map<String, Integer> get() {
		return columnsMappers;
	}
}
