package io.github.belmomusta.excel.importexport.objects;

import io.github.belmomusta.excel.importexport.annotation.ExcelCell;
import io.github.belmomusta.excel.importexport.annotation.ExcelRow;

@ExcelRow
public class CarWithAnnotations {
	@ExcelCell(0)
	private int id;
	@ExcelCell(1)
	private String name;
	@ExcelCell(2)
	private String model;
	
	public CarWithAnnotations() {}
	
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
