package io.github.belmomusta.exporter.processor.velocity;

import io.github.belmomusta.exporter.api.csv.AbstractCSVExporter;
import io.github.belmomusta.exporter.api.excel.AbstractExcelExporter;
import io.github.belmomusta.exporter.api.formatter.Formatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class VelocityConfig {
    private final boolean useFQNs;
    @SuppressWarnings("all")
    protected final Predicate<? super Class> classPredicate = c -> !isUseFQNs();
    private String fullCurrentClassName;
    
    
    public VelocityConfig(boolean useFQNs) {this.useFQNs = useFQNs;}
    
    public boolean isUseFQNs() {
        return useFQNs;
    }
    
    public String getFullCurrentClassName() {
        return fullCurrentClassName;
    }
    
    public void setFullCurrentClassName(String fullCurrentClassName) {
        this.fullCurrentClassName = fullCurrentClassName;
    }
    
    public String getCurrentClassName() {
        return Optional.of(fullCurrentClassName)
                .filter(c -> !isUseFQNs())
                .map(s -> s.substring(s.lastIndexOf('.') + 1))
                .orElse(fullCurrentClassName);
        
    }
    
    public String getSheetClassName() {
        return nameOfAClass(Sheet.class);
        
    }
    
    public String getAbstractCSVExporterClassName() {
        return nameOfAClass(AbstractCSVExporter.class);
    }
    
    public String getArrayListClassName() {
        return nameOfAClass(ArrayList.class);
    }
    
    public String getListClassName() {
        return nameOfAClass(List.class);
    }
    
    public String getListFullClassName() {
        return fullNameOfAClass(List.class);
    }
    
    public String getArraysFullClassName() {
        return fullNameOfAClass(Arrays.class);
    }
    
    public String getArraysClassName() {
        return nameOfAClass(Arrays.class);
    }
    
    public String getCollectionClassName() {
        return nameOfAClass(Collection.class);
    }
    
    public String getAbstractCSVExporterFullClassName() {
        return fullNameOfAClass(AbstractCSVExporter.class);
    }
    
    public String getArrayListFullClassName() {
        return fullNameOfAClass(ArrayList.class);
    }
    
    public String getCollectionFullClassName() {
        return fullNameOfAClass(Collection.class);
    }
    
    public String getRowClassName() {
        return nameOfAClass(Row.class);
    }
    
    public String getAbstractExcelMapperClassName() {
        return nameOfAClass(AbstractExcelExporter.class);
    }
    
    public String getSheetFullClassName() {
        return fullNameOfAClass(Sheet.class);
        
    }
    
    public String getRowFullClassName() {
        return fullNameOfAClass(Row.class);
    }
    
    public String getAbstractExcelMapperFullClassName() {
        return fullNameOfAClass(AbstractExcelExporter.class);
    }
    
    public String getStringClassName() {
        return nameOfAClass(String.class);
    }
    
    public String getFormatterClassName() {
        return nameOfAClass(Formatter.class);
    }
    
    public String getFormatterFullClassName() {
        return fullNameOfAClass(Formatter.class);
    }
    
    public String getSpringComponentAnnotation() {
        String componentAnnotation = "@org.springframework.stereotype.Component";
        try {
            Class.forName("org.springframework.stereotype.Component");
        } catch (ClassNotFoundException e) {
            componentAnnotation =
                    "/**\n WARNING : Your project must have Spring dependencies\n**/\n //" + componentAnnotation;
        }
        return componentAnnotation;
    }
    
    public String getAutowiredAnnotation() {
        String componentAnnotation = "@org.springframework.beans.factory.annotation.Autowired";
        try {
            Class.forName("org.springframework.beans.factory.annotation.Autowired");
        } catch (ClassNotFoundException e) {
            componentAnnotation =
                    "/**\n WARNING : Your project must have Spring dependencies\n**/\n //" + componentAnnotation;
        }
        return componentAnnotation;
    }
    
    
    private <X> String nameOfAClass(Class<X> value) {
        return Optional.of(value)
                .filter(classPredicate.negate())
                .map(Class::getName)
                .orElse(value.getSimpleName());
    }
    
    private <X> String fullNameOfAClass(Class<X> value) {
        return Optional.of(value)
                .filter(classPredicate)
                .map(Class::getName)
                .orElse(value.getSimpleName());
    }
    
}
