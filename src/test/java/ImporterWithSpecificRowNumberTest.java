import must.belmo.excel.importexport.ExcelImporter;
import must.belmo.excel.importexport.exception.ExcelImporterException;
import must.belmo.excel.importexport.objects.Car;
import must.belmo.excel.importexport.objects.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;

public class ImporterWithSpecificRowNumberTest {
	
	@Test
	public void testImportCars() throws ExcelImporterException {
		
		final String file = TestUtils.getCarsFile(getClass());
		final Collection<Car> cars = ExcelImporter.extract(Car.class)
				.from(file)
				.map("id").toCell(0)
				.map("name").toCell(1)
				.map("model").toCell(2)
				.itemsAtRows(0)
				.toCollection(new HashSet<>())
				.get();
		Assert.assertEquals(1, cars.size());
	}
	
	@Test
	public void testImportCarsMultipleIndex() throws ExcelImporterException {
		
		final String file = TestUtils.getCarsFile(getClass());
		final Collection<Car> cars = ExcelImporter.extract(Car.class)
				.from(file)
				.map("id").toCell(0)
				.map("name").toCell(1)
				.map("model").toCell(2)
				.itemsAtRows(0,1)
				.toCollection(new HashSet<>())
				.get();
		Assert.assertEquals(2, cars.size());
	}
}
