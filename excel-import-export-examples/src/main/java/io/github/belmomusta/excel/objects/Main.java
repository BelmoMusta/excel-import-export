package io.github.belmomusta.excel.objects;

import io.github.belmomusta.excel.api.CSVExporter;
import io.github.belmomusta.excel.api.ExcelExporter;
import io.github.belmomusta.excel.api.exception.ExcelExporterException;
import io.github.belmomusta.excel.object.excel.export.CarWithAnnotationsCSVExporter;
import io.github.belmomusta.excel.object.excel.export.CarWithAnnotationsExcelMapper;


public class Main {
	public static void main(String[] args) throws ExcelExporterException {
	ExcelExporter<CarWithAnnotations> excelExporter = new CarWithAnnotationsExcelMapper();
	CSVExporter<CarWithAnnotations> csvExporter = new CarWithAnnotationsCSVExporter();
		
		final CarWithAnnotations car = new CarWithAnnotations();
		car.setId(22);
		car.setModel("My modessssl");
		car.setName("a car name");
		car.setCity("POPO");
		car.setCountry("Maroc");
		final String destinationFile = ("cars.xlsx");
		excelExporter.exportToFile(car, destinationFile);
		csvExporter.exportToFile(car, "cars.csv");
		
	}
}
