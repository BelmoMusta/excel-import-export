package io.github.belmomusta.excel.importexport;

import java.util.List;

public interface ExcelMapper<T> {
    void extractToFile(List<T> t, java.io.File destFile);
}
