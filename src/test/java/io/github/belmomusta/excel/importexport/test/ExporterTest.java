package io.github.belmomusta.excel.importexport.test;

import io.github.belmomusta.excel.importexport.ExcelExporter;
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
		ExcelExporter.exportContent(Collections.singletonList(car))
				.toFile(destinationFile)
				.map("id").toCell(0)
				.map("name").toCell(1)
				.map("model").toCell(4)
				.withHeaders()
				.export();
		
		Assert.assertTrue(destinationFile.exists());
	}
	
	@Test
	public void testExportCarsWithMethods() throws ExcelExporterException {
		
		final Car car = new Car();
		car.setId(22);
		car.setModel("My model");
		car.setName("a car name");
		
		final File destinationFile = new File("cars-exported-with-method.xlsx");
		ExcelExporter.exportContent(Collections.singletonList(car))
				.toFile(destinationFile)
				.mapMethod("getId").toCell(0)
				.mapMethod("getName").toCell(1)
				.mapMethod("getModel").toCell(4)
				.withHeaders("id", "name", "model")
				.export();
		
		Assert.assertTrue(destinationFile.exists());
	}
	
	
	@After
	public void deleteTemporaryFile() throws Exception {
		File exportedCars = new File("cars-exported.xlx");
		File exportedCarsWithMethodNames = new File("cars-exported-with-method.xlsx");
		if (exportedCars.exists()) {
			FileUtils.forceDelete(exportedCars);
		}
		if (exportedCarsWithMethodNames.exists()) {
			FileUtils.forceDelete(exportedCarsWithMethodNames);
		}
	}
}
