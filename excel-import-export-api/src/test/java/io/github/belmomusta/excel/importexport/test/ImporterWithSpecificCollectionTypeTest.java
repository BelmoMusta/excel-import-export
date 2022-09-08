package io.github.belmomusta.excel.importexport.test;

import io.github.belmomusta.excel.importexport.ExcelImporter;
import io.github.belmomusta.excel.importexport.exception.ExcelImporterException;
import io.github.belmomusta.excel.importexport.objects.Car;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;

public class ImporterWithSpecificCollectionTypeTest {
	
	@Test
	public void testImportCars() throws ExcelImporterException {
		
		final String file = TestUtils.getCarsFile(getClass());
		final Collection<Car> cars = ExcelImporter.extract(Car.class)
				.from(file)
				.map("id").toCell(0)
				.map("name").toCell(1)
				.map("model").toCell(2)
				.toCollection(new HashSet<>())
				.get();
		Assert.assertEquals(HashSet.class, cars.getClass());
	}
}
