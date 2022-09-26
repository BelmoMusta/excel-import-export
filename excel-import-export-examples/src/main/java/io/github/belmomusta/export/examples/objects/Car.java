package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.CSV;
import io.github.belmomusta.exporter.api.annotation.ToColumn;
import io.github.belmomusta.exporter.api.annotation.Excel;

@Excel()
@CSV()
public class Car {
	@ToColumn
    private String name;
	@ToColumn(5)
    private String model;
	@ToColumn
    private int id;
	
	@ToColumn
    private int foo;
	
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

    @ToColumn
    public int getFoo() {
        return foo;
    }
	@ToColumn
	public String name(){
		return "name";
	}
}
