# Excel import and export

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.belmomusta/export-import-export/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.belmomusta/export-import-export)

This project contains utilities to import objects from an export file.
It also contains functions to export data from objects an to export file.

## Get it as a maven dependency  :

```XML
   <dependency>
            <groupId>io.github.belmomusta</groupId>
            <artifactId>export-import-export</artifactId>
            <version>1.1</version>
   </dependency>
```

Usage:

## 1. Importing

```java

final String file = "the path to a valid export file";
final Collection<Car> cars = ExcelImporter.extract(Car.class)
                .from(file)
                .inSheetNumber(0)
                .map("id").toCell(0)
                .map("name").toCell(1)
                .map("model").toCell(2)
                .get();
```

This snippet creates a collection of cars from the first sheet of an export workbook.
The importing is also possible with the annotation marker.
The object classes to import will have the `@ExcelCell` on the fields that we want to import.
For example :

```java
@ExcelRow // not supported yet
public class CarWithAnnotations {
	@ExcelCell(0)
	private int id;
	@ExcelCell(1)
	private String name;
	@ExcelCell(2)
	private String model;
	....
	 }
	Collection<CarWithAnnotations> cars = ExcelImporterAnnotation.extract(CarWithAnnotations.class)
				.from(file)
				.inSheetNumber(0)
				.get();
```

## 2. Exporting

To export a collection of items to an export file, You an use the `ExcelExporterService` to do so.
Here is a common example of use  :

### Without annotations

```java
  final Car car = new Car();
  car.setId(22);
  car.setModel("My model");
  car.setName("a car name");

 final File destinationFile = new File("cars-exported.xlsx");
 		ExcelExporterService.exportContent(Collections.singletonList(car))
 				.toFile(destinationFile)
 				.withHeaders()
 				.map("id").toCell(0)
 				.map("name").toCell(1)
 				.map("model").toCell(2)
 				.export();
```

### Export content using method names on the exported class

Used methods should have no args, and have a specific return value, void methods are not supported.

```java
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
				.withHeaders("id", "name", "model") // Here you van specify headers
				.export();
```

### With annotations

```java
final CarWithAnnotations car = new CarWithAnnotations();
		car.setId(22);
		car.setModel("My model");
		car.setName("a car name");

		final File destinationFile = new File("cars-exported-with-annotations.xlsx");
		ExcelExporterAnnotation.exportContent(Collections.singletonList(car))
				.toFile(destinationFile)
				.withHeaders("id", "name", "model")
				.export();
```

### Dependencies :

- Apache POI 3.15
- Apache Commons-IO 2.6
- Junit 4.13 for testing
