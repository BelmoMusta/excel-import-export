package io.github.belmomusta.excel.importexport.test;

import io.github.belmomusta.excel.importexport.ExcelExporterAnnotation;
import io.github.belmomusta.excel.importexport.exception.ExcelExporterException;
import io.github.belmomusta.excel.importexport.objects.CarWithAnnotations;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Collections;

public class ExporterWithAnnotationTest {
	@Test
	public void testExportCarsWithAnnotations() throws ExcelExporterException {
		
		final CarWithAnnotations car = new CarWithAnnotations();
		car.setId(22);
		car.setModel("My model");
		car.setName("a car name");
		
		final File destinationFile = new File("cars-exported-with-annotations.xlsx");
		ExcelExporterAnnotation.exportContent(Collections.singletonList(car))
				.toFile(destinationFile)
				.withHeaders("id", "name", "model")
				.export();
		Assert.assertTrue(destinationFile.exists());
	}
	
	@After
	public void deleteTemporaryFile() throws Exception {
		File exportedCarsWithMethodNames = new File("cars-exported-with-annotations.xlsx");
		if (exportedCarsWithMethodNames.exists()) {
			FileUtils.forceDelete(exportedCarsWithMethodNames);
		}
	}
}
