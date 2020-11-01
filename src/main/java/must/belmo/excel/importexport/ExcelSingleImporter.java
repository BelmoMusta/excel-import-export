package must.belmo.excel.importexport;

public class ExcelSingleImporter<T> extends ExcelImporter<T> {
	
	private final int row;
	private final int cell;
	
	private ExcelSingleImporter(Class<T> aClass, int row, int cell) {
		super(aClass);
		this.row = row;
		this.cell = cell;
	}
	
	public static <R> ExcelSingleImporter<R> extract(Class<R> aClass, int row, int cell) {
		return new ExcelSingleImporter<>(aClass, row, cell);
	}
	
}
