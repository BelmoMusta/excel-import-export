import must.belmo.excel.importexport.ExcelImporter;
import must.belmo.excel.importexport.exception.ExcelImporterException;
import must.belmo.excel.importexport.objects.Car;
import must.belmo.excel.importexport.objects.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ImporterTest {
	
	@Test
	public void testImportCars() throws ExcelImporterException {
		Map<String, Integer> map = new HashMap<>();
		map.put("id", 0);
		map.put("name", 1);
		map.put("model", 2);
		final String file = TestUtils.getCarsFile(getClass());
		final Collection<Car> cars = ExcelImporter.extract(Car.class)
				.from(file)
				.inSheetNumber(0)
				.withColumnsMapper(map)
				.get();
		
		Assert.assertEquals(2, cars.size());
		
		final Car firstCar = cars.iterator().next();
		Assert.assertEquals(1, firstCar.getId());
		Assert.assertEquals("Mercedes", firstCar.getName());
		Assert.assertEquals("AMG", firstCar.getModel());
		
	}
	
	@Test(expected = ExcelImporterException.class)
	public void testExcelImportException() throws ExcelImporterException {
		
		final String file = TestUtils.getCarsFile(getClass());
		Map<String, Integer> map = new HashMap<>();
		map.put("aFieldThatDoesNotExist", 1);
		final ExcelImporter<Car> carExcelImporterService = ExcelImporter.extract(Car.class)
				.withColumnsMapper(map)
				.from(file)
				.fromAllSheets();
		carExcelImporterService.get();
	}
	
}
