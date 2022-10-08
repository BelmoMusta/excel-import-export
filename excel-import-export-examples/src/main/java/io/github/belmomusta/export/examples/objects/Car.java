package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.CSV;
import io.github.belmomusta.exporter.api.annotation.ColumnFormat;
import io.github.belmomusta.exporter.api.annotation.ToColumn;
import io.github.belmomusta.exporter.api.annotation.Excel;

import java.util.Date;

@Excel(ignoreHeaders = false,useFQNs = true)
@CSV()
public class Car extends AbstractCar implements ICar, InterfaceToExcel {
	@ToColumn(name = "musta id")
	@ColumnFormat(formatter = IdConverter.class)
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
	
	@ColumnFormat(formatter = DateFormatter.class)
	@ToColumn
	private Date updateDate;
	@ToColumn
	@ColumnFormat(formatter = CurrencyFormatter.class)
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
	
	@ColumnFormat(formatter = DateFormatter.class)
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
		return null;
	}
	
	@Excel(ignoreHeaders = false)
	@CSV
public static class A{
		String j;
		@ToColumn
		public String getJ() {
			return j;
		}
	}
	
	@Excel
	@CSV
	public class AB extends ABC{
		String j;
		@ToColumn
		public String getJ() {
			return j;
		}
	}
	
	@Excel
	@CSV
	 class ABC{
		String j;
		@ToColumn
		public String getJJ() {
			return j;
		}
	}
	
	public MyEnumeration getEnumeration() {
		return enumeration;
	}
}
@Excel
class AnotherClass { // will be ignored because not public
	@ToColumn
	public String getInnerFileClass() {
		return "j";
	}
}
