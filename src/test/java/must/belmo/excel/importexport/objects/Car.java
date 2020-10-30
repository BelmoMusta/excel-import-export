package must.belmo.excel.importexport.objects;
@lombok.Getter
@lombok.Setter
public class Car {
	private String name;
	private String model;
	private int id;
	
	public Car() {}
	
	@Override
	public String toString() {
		return "must.belmo.excel.importexport.objects.Car{" +
				"name='" + name + '\'' +
				", model='" + model + '\'' +
				", id=" + id +
				'}';
	}
}
