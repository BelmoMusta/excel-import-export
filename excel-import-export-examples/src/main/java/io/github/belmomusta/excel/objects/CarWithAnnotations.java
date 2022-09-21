package io.github.belmomusta.excel.objects;

import io.github.belmomusta.excel.api.annotation.ExcelColumn;
import io.github.belmomusta.excel.api.annotation.ExcelRow;

import java.util.Date;

@ExcelRow(ignoreHeaders = true, useFQNs = true)
public class CarWithAnnotations {
    @ExcelColumn
    private String country;

    @ExcelColumn
    private String city;
    @ExcelColumn(0)
    private int id;
    @ExcelColumn(1)
    private String name;
    private String model;

   
    @Override
    @ExcelColumn(name = "KKKKK")
    public String toString() {
        return "must.belmo.excel.importexport.objects.Car{" +
                "name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", id=" + id +
                '}';
    }

    public String getName() {
        return this.name;
    }

    @ExcelColumn(2)
    public String getModel() {
        return this.model;
    }


    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @ExcelColumn(name = "foo")
    public Date getFoo(){
        return new Date();
    }
}
