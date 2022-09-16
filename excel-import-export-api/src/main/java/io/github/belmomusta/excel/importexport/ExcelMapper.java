package io.github.belmomusta.excel.importexport;

import java.util.Collection;
import java.util.List;

public interface ExcelMapper<T> {
    void extractToFile(Collection<T> t, java.io.File destFile);
}
