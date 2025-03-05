package io.github.mustabelmo.exporter.processor.velocity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class VelocityWrapper {
    private String aPackage;
    private String csvPackage;
    private String simplifiedClassName;
    private String className;
    private boolean withHeaders;
    private boolean useFQNs;
    private boolean withIoC;
    
    private Collection<FieldMethodPair> correspondanceFieldMethod;
    private List<String> imports;
    
    public String getSimplifiedClassName() {
        return simplifiedClassName;
    }
    
    public void setSimplifiedClassName(String simplifiedClassName) {
        this.simplifiedClassName = simplifiedClassName;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getCsvPackage() {
        return csvPackage;
    }
    
    public void setCsvPackage(String csvPackage) {
        this.csvPackage = csvPackage;
    }
    
    public String getAPackage() {
        return aPackage;
    }
    
    public void setAPackage(String aPackage) {
        this.aPackage = aPackage;
    }
    
    public Collection<FieldMethodPair> getCorrespondanceFieldMethod() {
        return correspondanceFieldMethod;
    }
    
    public void setCorrespondanceFieldMethod(Collection<FieldMethodPair> correspondanceFieldMethod) {
        this.correspondanceFieldMethod = correspondanceFieldMethod;
    }
    
    public boolean isWithHeaders() {
        return withHeaders;
    }
    
    public void setWithHeaders(boolean withHeaders) {
        this.withHeaders = withHeaders;
    }
    
    public boolean isUseFQNs() {
        return useFQNs;
    }
    
    public void setUseFQNs(boolean useFQNs) {
        this.useFQNs = useFQNs;
    }
    
    public Collection<FormatterWrapper> getFormatters() {
        return correspondanceFieldMethod.stream()
                .map(FieldMethodPair::getFormatter)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
    
    public boolean isWithIoC() {
        return withIoC;
    }
    
    public void setWithIoC(boolean withIoC) {
        this.withIoC = withIoC;
    }
    
    public void setImports(List<String> imports) {
        this.imports = imports;
    }
    
    public List<String> getImports() {
        return imports;
    }
}
