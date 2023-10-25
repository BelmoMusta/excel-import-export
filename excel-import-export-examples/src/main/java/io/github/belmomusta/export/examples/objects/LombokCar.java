package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.ColumnFormat;
import io.github.belmomusta.exporter.api.annotation.Export;
import io.github.belmomusta.exporter.api.annotation.ToColumn;
import io.github.belmomusta.exporter.api.common.ExportType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Export(type = {ExportType.EXCEL}, ignoreHeaders = false)
@Getter
@Setter
public class LombokCar {
    
    @ToColumn(name = "musta id")
    private int id;
    
    @ToColumn(9)
    @Getter(value = AccessLevel.NONE)
    private String name;
    
    @ToColumn(2)
    private String model;
    
    private int foo;
    
    @ToColumn(name = "Date de creation")
    private Date creationDate;
    
    @ToColumn
    private Date updateDate;
    
    @ToColumn(0)
    @ColumnFormat(formatter = CurrencyFormatter.class)
    private double price;
    
    @Override
    public String toString() {
        return "must.belmo.excel.importexport.objects.Car{" + "name='" + name + '\'' + ", model='" + model + '\'' +
                ", id=" + id + '}';
    }
    
    public String name() {
        return "name";
    }
    
    public String getModel() {
        return model;
    }
}
