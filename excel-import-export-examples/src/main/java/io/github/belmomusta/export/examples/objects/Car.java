package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.CSV;
import io.github.belmomusta.exporter.api.annotation.ExcelFormat;
import io.github.belmomusta.exporter.api.annotation.ToColumn;
import io.github.belmomusta.exporter.api.annotation.Excel;

import java.util.Date;

@Excel(ignoreHeaders = true)
@CSV()
public class Car {
	@ToColumn(9)
    private String name;
	@ToColumn
    private String model;
	@ToColumn
    private int id;
	
	@ToColumn
    private int foo;
	@ToColumn
	private Date creationDate;
	
	@ExcelFormat(formatter = DateFormatter.class)
	@ToColumn
	private Date updateDate;
	@ToColumn
	@ExcelFormat(formatter = CurrencyFormatter.class)
	private double price;
	
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
	public String name(){
		return "name";
	}
	
	@ExcelFormat(formatter = DateFormatter.class)
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public Date getUpdateDate() {
		return updateDate;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
}
