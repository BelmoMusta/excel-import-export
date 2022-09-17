package org.mustabelmo.validation.processor;

import io.github.belmomusta.excel.api.annotation.ExcelColumn;
import io.github.belmomusta.excel.api.annotation.ExcelRow;
import org.mustabelmo.validation.processor.velocity.FieldMethodPair;
import org.mustabelmo.validation.processor.velocity.VelocityWrapper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ClassWrapper {

    private Element annotatedElement;
    private List<MethodWrapper> enclosedMethods;
    private List<FieldWrapper> enclosedFields;
    private String fQNOfGeneratedClass;

    private ClassWrapper() {
    }

    public static ClassWrapper of(Element annotatedElement) {

        String generatedClassName = annotatedElement.getSimpleName().toString() + "ExcelMapper";
        Name qualifiedName = ((TypeElement) annotatedElement).getQualifiedName();
        ClassWrapper classWrapper = new ClassWrapper();
        classWrapper.annotatedElement = annotatedElement;
        classWrapper.enclosedMethods = annotatedElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .map(MethodWrapper::new)
                .collect(Collectors.toList());
        classWrapper.enclosedFields = annotatedElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .map(FieldWrapper::new)
                .collect(Collectors.toList());
        String aPackage = qualifiedName.toString();
        int index = aPackage.lastIndexOf('.');
        String packageName = aPackage.substring(0, index - 1);
        classWrapper.fQNOfGeneratedClass = packageName + ".excel.export." + generatedClassName;

        for (Class<? extends Annotation> annotation : AnnotationsRegistrer.ANNOTATIONS) {
            for (MethodWrapper methodWrapper : classWrapper.enclosedMethods) {
                Annotation foundAnnotation = methodWrapper.wrappedElement.getAnnotation(annotation);
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
        this.enclosedMethods.stream()
                .filter(MethodWrapper::isValid)
                .forEach(aMethod -> {
                    ExcelColumn annotation = aMethod.getAnnotation(ExcelColumn.class);
                    String possibleFieldNameForMethod = aMethod.getPossibleFieldName();
                    if (possibleFieldNameForMethod != null) {
                        FieldMethodPair fieldMethodPair = new FieldMethodPair(possibleFieldNameForMethod, aMethod.getName());
                        fff(annotation, possibleFieldNameForMethod, fieldMethodPair);
                        fieldMethodPairs.add(fieldMethodPair);
                    } else if(annotation != null){
                        ssss(fieldMethodPairs, aMethod, annotation);
                    }
                });

        return fieldMethodPairs;
    }
    
    private void ssss(Set<FieldMethodPair> fieldMethodPairs, MethodWrapper aMethod, ExcelColumn annotation) {
        FieldMethodPair methodPair = new FieldMethodPair(aMethod.getName());
        if(ExcelColumn.DEFAULT_NAME.equals(annotation.name())){
            methodPair.setHeaderName(aMethod.getName());
        } else {
            methodPair.setHeaderName(annotation.name());
        }
        methodPair.setOrder(annotation.value());
        fieldMethodPairs.add(methodPair);
    }
    
    private void fff(ExcelColumn annotation, String possibleFieldNameForMethod, FieldMethodPair fieldMethodPair) {
        if (annotation != null) {
            fieldMethodPair.setOrder(annotation.value());
            if(ExcelColumn.DEFAULT_NAME.equals(annotation.name())){
                fieldMethodPair.setHeaderName(possibleFieldNameForMethod);
            } else {
                fieldMethodPair.setHeaderName(annotation.name());
            }
        }
    }
    
    public String getFQNOfGeneratedClass() {
        return fQNOfGeneratedClass;
    }

    public VelocityWrapper getVelocityWrapper() {
        VelocityWrapper wrapper = new VelocityWrapper();
        wrapper.setSimplifiedClassName(annotatedElement.getSimpleName().toString());
        ExcelRow excelRow = annotatedElement.getAnnotation(ExcelRow.class);
        wrapper.setUseFQNs(excelRow.useFQNs());
        if (!excelRow.ignoreHeaders()) {
            wrapper.setWithHeaders(true);
        }
        if (annotatedElement instanceof TypeElement) {
            Name qualifiedName = ((TypeElement) annotatedElement).getQualifiedName();
            String aPackage = getFQNOfGeneratedClass();
            int index = aPackage.lastIndexOf('.');
            String packageName = aPackage.substring(0, index);
            wrapper.setAPackage(packageName);
            wrapper.setClassName(qualifiedName.toString());

            Collection<FieldMethodPair> correspondanceFieldMethod = getCorrespondanceFieldMethod();
            wrapper.setCorrespondanceFieldMethod(correspondanceFieldMethod);

        }
        return wrapper;
    }
}
