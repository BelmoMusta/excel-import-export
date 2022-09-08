package io.github.belmomusta.excel.importexport;

import java.util.Collection;

public interface ExcelMapper<T> {
    void extractToFile(Collection<T> t, java.io.File destFile);
}
