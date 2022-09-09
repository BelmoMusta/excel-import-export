package org.mustabelmo.validation.processor;

import java.util.Collection;
import java.util.List;

public class VelocityWrapper {
    private String aPackage;
    private String simplifiedClassName;
    private String className;

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
}
