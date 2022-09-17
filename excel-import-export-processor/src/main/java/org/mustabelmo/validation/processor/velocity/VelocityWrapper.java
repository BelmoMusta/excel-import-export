package org.mustabelmo.validation.processor.velocity;

import org.mustabelmo.validation.processor.DefaultParamNameStrategy;
import org.mustabelmo.validation.processor.ParamNameStrategy;

import java.util.Collection;

public class VelocityWrapper {
    private String aPackage;
    private String simplifiedClassName;
    private String className;
    private boolean withHeaders;
    private boolean useFQNs;
    
    private ParamNameStrategy paramNameStrategy = new DefaultParamNameStrategy();
    private Collection<FieldMethodPair> correspondanceFieldMethod;

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

    public String getParamName() {
        return paramNameStrategy.getParam(simplifiedClassName);
    }

    public void setAPackage(String aPackage) {
        this.aPackage = aPackage;
    }

    public String getAPackage() {
        return aPackage;
    }

    public void setParamNameStrategy(ParamNameStrategy paramNameStrategy) {
        this.paramNameStrategy = paramNameStrategy;
    }

    public void setCorrespondanceFieldMethod(Collection<FieldMethodPair> correspondanceFieldMethod) {
        this.correspondanceFieldMethod = correspondanceFieldMethod;
    }

    public Collection<FieldMethodPair> getCorrespondanceFieldMethod() {
        return correspondanceFieldMethod;
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
}
