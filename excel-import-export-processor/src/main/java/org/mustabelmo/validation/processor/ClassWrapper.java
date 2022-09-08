package org.mustabelmo.validation.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassWrapper {

    private final String generatedClassName;
    private Element annotatedElement;
    private List<WrappedElement> enclosedElements;

    public ClassWrapper(String generatedClassName) {
        this.generatedClassName = generatedClassName;
    }

    public static ClassWrapper of(Element annotatedElement) {

        String generatedClassName = annotatedElement.getSimpleName().toString() + "ExcelMapper";
        ClassWrapper classWrapper = new ClassWrapper(generatedClassName);
        classWrapper.annotatedElement = annotatedElement;
        return classWrapper;
    }

    public String getParameterName() {
        String parameterType = annotatedElement.getSimpleName().toString();
        return "p" + parameterType;
    }

    public List<WrappedElement> getElements() {
        return enclosedElements;
    }


    public Map<String, WrappedElement> getCorrespondanceFieldMethod() {
        Map<String, WrappedElement> methodFieldMapper = new LinkedHashMap<>();
        for (WrappedElement enclosedElement : this.enclosedElements) {
            if (enclosedElement.isMethod() && enclosedElement.isValid()) {
                methodFieldMapper.put(enclosedElement.getPossibleFieldNameForMethod(), enclosedElement);
            }
        }
        return methodFieldMapper;
    }

    public void setAnnotatedElement(Element annotatedElement) {
        this.annotatedElement = annotatedElement;
        this.enclosedElements = annotatedElement.getEnclosedElements().stream()
                .map(WrappedElement::new)
                .collect(Collectors.toList());
    }

    public String getGeneratedClassName() {
        return generatedClassName;
    }

    public VelocityWrapper getVelocityWrapper() {
        VelocityWrapper wrapper = new VelocityWrapper();
        wrapper.setSimplifiedClassName(annotatedElement.getSimpleName().toString());
        if (annotatedElement instanceof TypeElement) {
            Name qualifiedName = ((TypeElement) annotatedElement).getQualifiedName();
            String aPackage = qualifiedName.toString();
            int index = aPackage.lastIndexOf('.');
            wrapper.setAPackage(aPackage.substring(0, index - 1));
            wrapper.setClassName(qualifiedName.toString());

        }
        return wrapper;
    }

}
