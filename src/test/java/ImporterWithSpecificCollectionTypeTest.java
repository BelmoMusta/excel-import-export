import must.belmo.excel.importexport.ExcelImporter;
import must.belmo.excel.importexport.exception.ExcelImporterException;
import must.belmo.excel.importexport.objects.Car;
import must.belmo.excel.importexport.objects.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;

public class ImporterWithSpecificCollectionTypeTest {
	
	@Test
	public void testImportCars() throws ExcelImporterException {
		
		final String file = TestUtils.getCarsFile(getClass());
		final Collection<Car> cars = ExcelImporter.extract(Car.class)
				.from(file)
				.map("id").toCell(0)
				.map("name").toCell(1)
				.map("model").toCell(2)
				.toCollection(new HashSet<>())
				.get();
		Assert.assertEquals(HashSet.class, cars.getClass());
	}
}
