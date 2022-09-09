package io.github.belmomusta.excel.objects;

import io.github.belmomusta.excel.importexport.annotation.ExcelColumn;
import io.github.belmomusta.excel.importexport.annotation.ExcelRow;

@ExcelRow
public class CarWithAnnotations {
    @ExcelColumn(0)
    private int id;
    @ExcelColumn(1)
    private String name;
    @ExcelColumn(2)
    private String model;

    public CarWithAnnotations() {
    }

    @Override
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
}
