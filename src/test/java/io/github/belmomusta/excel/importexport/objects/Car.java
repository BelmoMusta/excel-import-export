package io.github.belmomusta.excel.importexport.objects;

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
	
	public String getName() {return this.name;}
	
	public String getModel() {return this.model;}
	
	public int getId() {return this.id;}
	
	public void setName(String name) {this.name = name; }
	
	public void setModel(String model) {this.model = model; }
	
	public void setId(int id) {this.id = id; }
}
