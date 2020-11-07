package io.github.belmomusta.excel.importexport.test;

import io.github.belmomusta.excel.importexport.ExcelImporterAnnotation;
import io.github.belmomusta.excel.importexport.exception.ExcelImporterException;
import io.github.belmomusta.excel.importexport.objects.CarWithAnnotations;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ImporterWithAnnotationTest {
	
	@Test
	public void testImportCars() throws ExcelImporterException {
		
		final String file = TestUtils.getCarsFile(getClass());
		
		Collection<CarWithAnnotations> cars = ExcelImporterAnnotation.extract(CarWithAnnotations.class)
				.from(file)
				.inSheetNumber(0)
				.get();
		
		Assert.assertEquals(2, cars.size());
		
		final CarWithAnnotations firstCar = cars.iterator().next();
		Assert.assertEquals(1, firstCar.getId());
		Assert.assertEquals("Mercedes", firstCar.getName());
		Assert.assertEquals("AMG", firstCar.getModel());
		
	}
	
	@Test()
	public void testExcelImportException() throws ExcelImporterException {
		
		Map<String, Integer> map = new HashMap<>();
		map.put("aFieldThatDoesNotExist", 1);
		final String file = TestUtils.getCarsFile(getClass());
		ExcelImporterAnnotation.extract(CarWithAnnotations.class)
				.from(file)
				.inSheetNumber(0)
				.get();
	}
	
}
