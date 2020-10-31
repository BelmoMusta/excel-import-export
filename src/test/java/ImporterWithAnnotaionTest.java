import must.belmo.excel.importexport.ExcelImporterAnnotationService;
import must.belmo.excel.importexport.exception.ExcelImporterException;
import must.belmo.excel.importexport.objects.CarWithAnnotations;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ImporterWithAnnotaionTest {
	
	@Test
	public void testImportCars() throws ExcelImporterException {
		
		final ExcelImporterAnnotationService<CarWithAnnotations> carExcelImporterService =
				new ExcelImporterAnnotationService<>();
		
		final String file = getCarsFile();
		final Collection<CarWithAnnotations> cars =
				carExcelImporterService.importFromExcelFile(CarWithAnnotations.class, file, 0);
		Assert.assertEquals(2, cars.size());
		
		final CarWithAnnotations firstCar = cars.iterator().next();
		Assert.assertEquals(1, firstCar.getId());
		Assert.assertEquals("Mercedes", firstCar.getName());
		Assert.assertEquals("AMG", firstCar.getModel());
		
	}
	
	@Test()
	public void testExcelImportException() throws ExcelImporterException {
		
		final ExcelImporterAnnotationService<CarWithAnnotations> carExcelImporterService =
				new ExcelImporterAnnotationService<>();
		Map<String, Integer> map = new HashMap<>();
		map.put("aFieldThatDoesNotExist", 1);
		final String file = getCarsFile();
		carExcelImporterService.importFromExcelFile(CarWithAnnotations.class, file, 0);
	}
	
	private String getCarsFile() {
		final URL resource = getClass().getClassLoader().getResource("cars.xlsx");
		return resource.getFile();
	}
}
