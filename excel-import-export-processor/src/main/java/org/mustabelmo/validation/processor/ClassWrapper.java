package org.mustabelmo.validation.processor;

import io.github.belmomusta.excel.importexport.annotation.ExcelColumn;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ClassWrapper {

    private final String generatedClassName;
    private Element annotatedElement;
    private List<MethodWrapper> enclosedMethods;

    private List<FieldWrapper> enclosedFields;

    private ClassWrapper(String generatedClassName) {
        this.generatedClassName = generatedClassName;
    }

    public static ClassWrapper of(Element annotatedElement) {

        String generatedClassName = annotatedElement.getSimpleName().toString() + "ExcelMapper";
        ClassWrapper classWrapper = new ClassWrapper(generatedClassName);
        classWrapper.annotatedElement = annotatedElement;
        classWrapper.enclosedMethods = annotatedElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .map(MethodWrapper::new)
                .collect(Collectors.toList());
        classWrapper.enclosedFields = annotatedElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .map(FieldWrapper::new)
                .collect(Collectors.toList());
        for (Class<? extends Annotation> annotation : AnnotationsRegistrer.ANNOTATIONS) {
            for (MethodWrapper methodWrapper : classWrapper.enclosedMethods) {
                Annotation foundAnnotation = methodWrapper.wrappedMethod.getAnnotation(annotation);
                if (foundAnnotation == null) {
                    foundAnnotation = classWrapper.lookForAnnotationInPossibleField(methodWrapper);
                    if (foundAnnotation != null) {
                        methodWrapper.add(foundAnnotation);
                    }
                }
            }
        }
        return classWrapper;
    }

    private Annotation lookForAnnotationInPossibleField(MethodWrapper methodWrapper) {
        String possibleFieldName = methodWrapper.getPossibleFieldName();
        for (FieldWrapper enclosedField : this.enclosedFields) {
            if (enclosedField.getName().equals(possibleFieldName)) {
                return enclosedField.getAnnotation(ExcelColumn.class);
            }
        }
        return null;
    }


    public Collection<FieldMethodPair> getCorrespondanceFieldMethod() {
        Set<FieldMethodPair> fieldMethodPairs = new TreeSet<>();
        for (MethodWrapper aMethod : this.enclosedMethods) {
            if (aMethod.isMethod() && aMethod.isValid()) {
                String possibleFieldNameForMethod = aMethod.getPossibleFieldName();
                if (possibleFieldNameForMethod != null) {
                    FieldMethodPair fieldMethodPair = new FieldMethodPair(possibleFieldNameForMethod, aMethod.getName());
                    ExcelColumn annotation = aMethod.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        fieldMethodPair.setOrder(annotation.value());
                    }
                    fieldMethodPairs.add(fieldMethodPair);
                }
            }
        }

        return new ArrayList<>(fieldMethodPairs);
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

            Collection<FieldMethodPair> correspondanceFieldMethod = getCorrespondanceFieldMethod();
            wrapper.setCorrespondanceFieldMethod(correspondanceFieldMethod);

        }
        return wrapper;
    }

}
