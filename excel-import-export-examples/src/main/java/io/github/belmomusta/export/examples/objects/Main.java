package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.export.examples.objects.export.excel.CarExcelExporter;
import io.github.belmomusta.exporter.api.common.CommonExporter;
import io.github.belmomusta.exporter.api.exception.ExporterException;

import java.util.Arrays;
import java.util.Date;


public class Main {
	public static void main(String[] args) throws ExporterException {
		CommonExporter<Car> excelExporter = new CarExcelExporter();
		
		final Car car = new Car();
		car.setId(22);
		car.setModel("My new model");
		car.setName("Porsche");
		car.setCreationDate(new Date());
		car.setPrice(300000.99);
		final String destinationFile = ("cars.xlsx");
		excelExporter.exportToFile(Arrays.asList(car,car), destinationFile);
	}
}
