# Excel import and export
This project contains utilities to import objects from an excel file.
It also contains functions to export data from objects an to excel file.

Usage:
## 1. Importing

```java
final Map<String, Integer> columnsMapper = new HashMap<>();
		map.put("id", 0);
		map.put("name", 1);
		map.put("model", 2);
		final String file = "the path to a valid excel file";
		final Collection<Car> cars = ExcelImporter.extract(Car.class)
				.from(file)
				.inSheetNumber(0)
				.withColumnsMapper(columnsMapper)
				.doImport()
				.get();
```
This snippet creates a collection of cars from the first sheet of an excel workbook.
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
				.doImport()
				.get();
```

## 2.  Exporting
    To export a collection of items to an excel file, You an use the `ExcelExporterService` to do so.
    Here is a common example of use  :

```java
  final Car car = new Car();
  car.setId(22);
  car.setModel("My model");
  car.setName("a car name");

  final Map<String, Integer> columnsMapper = new HashMap<>();
  columnsMapper.put("id", 0);
  columnsMapper.put("name", 1);
  columnsMapper.put("model", 2);


  final File destinationFile = new File("cars-exported.xlsx");
  ExcelExporterService.exportContent(Collections.singletonList(car))
        .toFile(destinationFile)
        .withColumnsMappers(columnsMapper)
        .withHeaders()
        .export();
```

### Dependencies :
 - Apache POI 3.15
 - Apache Commons-IO 2.6
 - Junit 4.13 for testing