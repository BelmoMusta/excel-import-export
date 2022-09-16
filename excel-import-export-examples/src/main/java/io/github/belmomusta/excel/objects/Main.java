package io.github.belmomusta.excel.objects;

import io.github.belmomusta.excel.importexport.ExcelMapper;
import io.github.belmomusta.excel.object.excel.export.CarWithAnnotationsExcelMapper;

import java.io.File;
import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
	ExcelMapper<CarWithAnnotations> excelMapper = new CarWithAnnotationsExcelMapper();
		
		final CarWithAnnotations car = new CarWithAnnotations();
		car.setId(22);
		car.setModel("My modessssl");
		car.setName("a car name");
		car.setCity("POPO");
		car.setCountry("Maroc");
		
		final File destinationFile = new File("cars.xlsx");
		excelMapper.extractToFile(Arrays.asList(car), destinationFile);
		
	}
}
