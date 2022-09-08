package io.github.belmomusta.excel.importexport;

import java.util.LinkedHashMap;

public class ColumnsCellsMap extends LinkedHashMap<String, Integer> {
    private String currentColumnName;

    public ColumnsCellsMap map(String columnName) {
        currentColumnName = columnName;
        return this;
    }

    public ColumnsCellsMap toCell(Integer cellNumber) {
        put(currentColumnName, cellNumber);
        return this;
    }

}