package io.github.belmomusta.excel.importexport.test;

import io.github.belmomusta.excel.importexport.ExcelImporter;
import io.github.belmomusta.excel.importexport.exception.ExcelImporterException;
import io.github.belmomusta.excel.importexport.objects.Car;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ImporterTest {
	
	@Test
	public void testImportCars() throws ExcelImporterException {
		Map<String, Integer> map = new HashMap<>();
		
		final String file = TestUtils.getCarsFile(getClass());
		final Collection<Car> cars = ExcelImporter.extract(Car.class)
				.from(file)
				.inSheetNumber(0)
				.map("id").toCell(0)
				.map("name").toCell(1)
				.map("model").toCell(2)
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
		final ExcelImporter<Car> carExcelImporterService = ExcelImporter.extract(Car.class)
				.from(file)
				.map("aFieldThatDoesNotExist").toCell( 1)
				.fromAllSheets();
		carExcelImporterService.get();
	}
	
}
