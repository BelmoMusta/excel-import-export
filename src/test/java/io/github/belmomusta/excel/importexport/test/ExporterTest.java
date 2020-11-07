package io.github.belmomusta.excel.importexport.test;

import io.github.belmomusta.excel.importexport.ExcelExporterService;
import io.github.belmomusta.excel.importexport.exception.ExcelExporterException;
import io.github.belmomusta.excel.importexport.objects.Car;
import org.apache.commons.io.FileUtils;
import org.junit.After;
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
	
	@After
	public void deleteTemporaryFile() throws Exception{
		FileUtils.forceDelete(new File("cars-exported.xlsx"));
	}
}