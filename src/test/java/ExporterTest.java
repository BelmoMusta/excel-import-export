import must.belmo.excel.importexport.ExcelExporterService;
import must.belmo.excel.importexport.exception.ExcelExporterException;
import must.belmo.excel.importexport.objects.Car;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Collections;

public class ExporterTest {
	
	@Test
	public void testExportCars() throws ExcelExporterException {
		
		final Car car = new Car();
		car.setId(22);
		car.setModel("My model");
		car.setName("a car name");
		
		final File destinationFile = new File("cars-exported.xlsx");
		ExcelExporterService.exportContent(Collections.singletonList(car))
				.toFile(destinationFile)
				.map("id").toCell(0)
				.map("name").toCell(1)
				.map("model").toCell(4)
				.withHeaders()
				.export();
		
		Assert.assertTrue(destinationFile.exists());
	}
}
