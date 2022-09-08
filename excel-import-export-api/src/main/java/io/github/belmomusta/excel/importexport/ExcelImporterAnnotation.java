package io.github.belmomusta.excel.importexport;

public class ExcelImporterAnnotation<T> extends ExcelImporter<T> {

    private ExcelImporterAnnotation(Class<T> aClass) {
        super(aClass);
        mapAnnotatedMembers(aClass);
    }

    public static <R> ExcelImporterAnnotation<R> extract(Class<R> aClass) {
        return new ExcelImporterAnnotation<>(aClass);
    }
}
