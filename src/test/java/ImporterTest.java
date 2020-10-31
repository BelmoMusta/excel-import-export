import must.belmo.excel.importexport.ExcelImporterService;
import must.belmo.excel.importexport.exception.ExcelImporterException;
import must.belmo.excel.importexport.objects.Car;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ImporterTest {
	
	@Test
	public void testImportCars() throws ExcelImporterException {
		
		final ExcelImporterService<Car> carExcelImporterService = new ExcelImporterService<>();
		Map<String, Integer> map = new HashMap<>();
		map.put("id", 0);
		map.put("name", 1);
		map.put("model", 2);
		final String file = getCarsFile();
		final Collection<Car> cars = carExcelImporterService.importFromExcelFile(Car.class, file, 0, map);
		Assert.assertEquals(2, cars.size());
		
		final Car firstCar = cars.iterator().next();
		Assert.assertEquals(1, firstCar.getId());
		Assert.assertEquals("Mercedes", firstCar.getName());
		Assert.assertEquals("AMG", firstCar.getModel());
		
	}
	
	@Test(expected = ExcelImporterException.class)
	public void testExcelImportException() throws ExcelImporterException {
		
		final ExcelImporterService<Car> carExcelImporterService = new ExcelImporterService<>();
		Map<String, Integer> map = new HashMap<>();
		map.put("aFieldThatDoesNotExist", 1);
		final String file = getCarsFile();
		carExcelImporterService.importFromExcelFile(Car.class, file, 0, map);
	}
	
	private String getCarsFile() {
		final URL resource = getClass().getClassLoader().getResource("cars.xlsx");
		return resource.getFile();
	}
}
