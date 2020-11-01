import must.belmo.excel.importexport.ExcelImporter;
import must.belmo.excel.importexport.exception.ExcelImporterException;
import must.belmo.excel.importexport.objects.Car;
import must.belmo.excel.importexport.objects.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ImporterWithSpecificCollectionTypeTest {
	
	@Test
	public void testImportCars() throws ExcelImporterException {
		final Map<String, Integer> map = new HashMap<>();
		map.put("id", 0);
		map.put("name", 1);
		map.put("model", 2);
		final String file = TestUtils.getCarsFile(getClass());
		final Collection<Car> cars = ExcelImporter.extract(Car.class)
				.from(file)
				.withColumnsMapper(map)
				.doImport()
				.toCollection(new HashSet<>())
				.get();
		Assert.assertEquals(HashSet.class, cars.getClass());
	}
}
