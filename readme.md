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
  `TODO`

### Dependencies :
 - Apache POI 3.15
 - Apache Commons-IO 2.6
 - Junit 4.13 for testing
