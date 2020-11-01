import must.belmo.excel.importexport.ExcelExporterService;
import must.belmo.excel.importexport.exception.ExcelExporterException;
import must.belmo.excel.importexport.objects.Car;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExporterTest {
	
	@Test
	public void testExportCars() throws ExcelExporterException {
		
		final Car car = new Car();
		car.setId(22);
		car.setModel("My model");
		car.setName("a car name");
		
		final Map<String, Integer> columnsMapper = new HashMap<>();
		columnsMapper.put("id", 0);
		columnsMapper.put("name", 1);
		columnsMapper.put("model", 2);
		
		
		final File destinationFile = new File("cars-exported.xlsx");
		ExcelExporterService.exportContent(Collections.singletonList(car))
				.toFile(destinationFile)
				.withColumnsMappers(columnsMapper)
				.withHeaders()
				.export();
		
		Assert.assertTrue(destinationFile.exists());
	}
}
