package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.CSV;
import io.github.belmomusta.exporter.api.annotation.ColumnFormat;
import io.github.belmomusta.exporter.api.annotation.ToColumn;
import io.github.belmomusta.exporter.api.annotation.Excel;

import java.util.Date;

@Excel(ignoreHeaders = true,useFQNs = false)
@CSV()
public class Car implements InterfaceToExcel {
	@ToColumn(name = "musta id")
 	private int id;
	@ToColumn(9)
    private String name;
	
	@ToColumn(9)
    private MyEnumeration enumeration;
	@ToColumn
    private String model;

    private int foo;
	@ToColumn
	private Date creationDate;
	
 	@ToColumn
	private Date updateDate;
	@ToColumn
	@ColumnFormat(formatter = CurrencyFormatter.class)
	private double price;
	
	Inner inner;
	@Excel
	@ToColumn
	public Inner getInner() {
		return inner;
	}
	
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
    public static int getFoo() {
        return 1;
    }
	public String name(){
		return "name";
	}
	
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
	
	@Override
	public String getSomething() {
		return "something";
	}
	
	public MyEnumeration getEnumeration() {
		return enumeration;
	}
}
