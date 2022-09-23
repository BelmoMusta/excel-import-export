package io.github.belmomusta.export.examples.objects;

//import io.github.belmomusta.export.examples.objects.csv.export.CarCSVExporter;
//import io.github.belmomusta.export.examples.objects.excel.export.CarExcelExporter;
import io.github.belmomusta.exporter.api.csv.CSVExporter;
import io.github.belmomusta.exporter.api.excel.ExcelExporter;
import io.github.belmomusta.exporter.api.exception.ExporterException;


public class Main {
	public static void main(String[] args) throws ExporterException {
		//ExcelExporter<Car> excelExporter = new CarExcelExporter();
		//CSVExporter<Car> csvExporter = new CarCSVExporter();
		
		final Car car = new Car();
		car.setId(22);
		car.setModel("My model");
		car.setName("a car name");
		final String destinationFile = ("cars.xlsx");
		//excelExporter.exportToFile(car, destinationFile);
		//csvExporter.exportToFile(car, "cars.csv");
		
	}
}
