package org.mustabelmo.validation.processor;

import io.github.belmomusta.excel.importexport.annotation.ExcelColumn;
import io.github.belmomusta.excel.importexport.annotation.ExcelRow;
import org.mustabelmo.validation.processor.velocity.FieldMethodPair;
import org.mustabelmo.validation.processor.velocity.Header;
import org.mustabelmo.validation.processor.velocity.VelocityWrapper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        for (MethodWrapper aMethod : this.enclosedMethods) {
            if (aMethod.isValid()) {
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

        return fieldMethodPairs;
    }

    public String getFQNOfGeneratedClass() {
        return fQNOfGeneratedClass;
    }

    public VelocityWrapper getVelocityWrapper() {
        VelocityWrapper wrapper = new VelocityWrapper();
        List<Header> headers = lookForHeaders();
        wrapper.setHeaders(headers);
        wrapper.setSimplifiedClassName(annotatedElement.getSimpleName().toString());
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

    public  List<Header> lookForHeaders() {

        ExcelRow excelRow = annotatedElement.getAnnotation(ExcelRow.class);
        if (excelRow.ignoreHeaders()) {
            return Collections.emptyList();
        }

        List<Header> headers = new ArrayList<>();
        List<String> fieldsNotHavingHeaders = new ArrayList<>();
        List<String> processedFields = new ArrayList<>();
        for (FieldWrapper enclosedField : this.enclosedFields) {
            ExcelColumn excelColumn = enclosedField.getAnnotation(ExcelColumn.class);
            if (excelColumn != null) {
                String headerName = excelColumn.name();
                int order = excelColumn.value();
                if (ExcelColumn.DEFAULT_NAME.equals(headerName)) {
                    headerName = enclosedField.getName();
                }
                Header header = new Header(headerName, order);
                processedFields.add(enclosedField.getName());
                headers.add(header);
            } else {
                fieldsNotHavingHeaders.add(enclosedField.getName());
            }
        }

        for (MethodWrapper enclosedMethod : this.enclosedMethods) {
            ExcelColumn excelColumnOnMethod = enclosedMethod.getAnnotation(ExcelColumn.class);
            if (excelColumnOnMethod != null) {
                boolean methodHasField = false;
                for (String fieldName : fieldsNotHavingHeaders) {
                    if (fieldName.equals(enclosedMethod.getPossibleFieldName())) {
                        String headerName = excelColumnOnMethod.name();
                        if (ExcelColumn.DEFAULT_NAME.equals(headerName)) {
                            headerName = fieldName;
                        }
                        Header header = new Header(headerName, excelColumnOnMethod.value());
                        headers.add(header);
                        methodHasField = true;
                        break;
                    }
                }
                if (!methodHasField && !processedFields.contains(enclosedMethod.getPossibleFieldName())) {
                    String headerName;
                    if (ExcelColumn.DEFAULT_NAME.equals(excelColumnOnMethod.name())) {
                        headerName = enclosedMethod.getName();
                    } else {
                        headerName = excelColumnOnMethod.name();
                    }
                    Header header = new Header(headerName, excelColumnOnMethod.value());
                    headers.add(header);
                }
            }
        }
        return headers;
    }

}
