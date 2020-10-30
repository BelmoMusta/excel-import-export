package must.belmo.excel.importexport.functional;
import java.util.function.Function;

@FunctionalInterface
public interface Extractor<T, R> extends Function<T, R> {
	default R extract(T t) {
		return apply(t);
	}
}
