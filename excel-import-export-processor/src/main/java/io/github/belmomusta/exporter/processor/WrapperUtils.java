package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.api.annotation.ObjectToColumns;
import io.github.belmomusta.exporter.api.annotation.ToColumn;
import io.github.belmomusta.exporter.api.common.ExportType;
import io.github.belmomusta.exporter.processor.types.FieldMethodSet;
import io.github.belmomusta.exporter.processor.velocity.FieldMethodPair;
import io.github.belmomusta.exporter.processor.velocity.VelocityWrapper;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WrapperUtils {
    
    private WrapperUtils() {
        throw new IllegalStateException("not to be instantiated");
    }
    
    static void assignAnnotations(ClassWrapper classWrapper) {
        for (Class<? extends Annotation> annotation : AnnotationsRegistrer.ANNOTATIONS) {
            for (MethodWrapper methodWrapper : classWrapper.getEnclosedMethods()) {
                Annotation foundAnnotation = methodWrapper.wrappedElement.getAnnotation(annotation);
                if (foundAnnotation == null) {
                    foundAnnotation = lookForAnnotationInPossibleField(classWrapper, methodWrapper);
                    if (foundAnnotation != null) {
                        methodWrapper.add(foundAnnotation);
                    }
                }
            }
        }
    }
    
    private static Annotation lookForAnnotationInPossibleField(ClassWrapper classWrapper, MethodWrapper methodWrapper) {
        String possibleFieldName = methodWrapper.getPossibleFieldName();
        for (FieldWrapper enclosedField : classWrapper.getEnclosedFields()) {
            if (enclosedField.getName().equals(possibleFieldName)) {
                return enclosedField.getAnnotation(ToColumn.class);
            }
        }
        return null;
    }
    
    static String calculatePackageName(TypeElement annotatedElement) {
        PackageElement packageElement =
                ExportProcessor.processingEnvironment.getElementUtils().getPackageOf(annotatedElement);
        return packageElement.getQualifiedName().toString();
        
    }
    
    static void fillMethods(ClassWrapper classWrapper) {
        final List<MethodWrapper> methodWrappers = new ArrayList<>();
        if (classWrapper.isWithLombok()) {
            for (FieldWrapper enclosedField : classWrapper.getEnclosedFields()) {
                MethodWrapper derivedWrapper = new LombokMethodWrapper(enclosedField.wrappedElement);
                methodWrappers.add(derivedWrapper);
            }
        }
        
        for (FieldWrapper enclosedField : classWrapper.getEnclosedFields()) {
            if (enclosedField.isFieldAnnotatedWithLombokGetter()) {
                MethodWrapper derivedWrapper = new LombokMethodWrapper(enclosedField.wrappedElement);
                methodWrappers.add(derivedWrapper);
            }
        }
        List<MethodWrapper> declaredMethods = classWrapper.getAnnotatedElement()
                .getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .map(MethodWrapper::new)
                .collect(Collectors.toList());
        methodWrappers.addAll(declaredMethods);
        classWrapper.setEnclosedMethods(methodWrappers);
    }
    
    static void fillFQN(ClassWrapper classWrapper, String packagePrefix, String classSuffix) {
        String generatedClassName = calculateGeneratedClassName(classSuffix, classWrapper);
        String packageName = calculatePackageName((TypeElement) classWrapper.getAnnotatedElement());
        if (packageName.isEmpty()) {
            classWrapper.setFQNOfGeneratedClass(generatedClassName);
        } else {
            classWrapper.setFQNOfGeneratedClass(packageName + packagePrefix + generatedClassName);
        }
    }
    
    private static String calculateGeneratedClassName(String classSuffix, ClassWrapper classWrapper) {
        return classWrapper.getAnnotatedElement().getSimpleName() + classSuffix;
    }
    
    static void getInheritedMethods(ClassWrapper classWrapper, Collection<ClassWrapper> wrappers) {
        for (ClassWrapper wrapper : wrappers) {
            Collection<FieldMethodPair> correspondanceFieldMethod = getFieldMethodPairs(wrapper);
            Collection<FieldMethodPair> inheritedMetods;
            if (wrapper.getAnnotatedElement().getKind() == ElementKind.INTERFACE) {
                //  do not take static methods if it is an interface
                inheritedMetods = correspondanceFieldMethod.stream()
                        .filter(m -> !m.isStaticMethod())
                        .collect(Collectors.toList());
            } else {
                inheritedMetods = correspondanceFieldMethod;
            }
            
            classWrapper.getInheritedMembers().addAll(inheritedMetods);
        }
    }
    
    static List<Element> lookForSuperClasses(TypeMirror annotatedElement) {
        List<Element> objects = new ArrayList<>();
        List<? extends TypeMirror> typeMirrors =
                ExportProcessor.processingEnvironment.getTypeUtils().directSupertypes(annotatedElement);
        for (TypeMirror supertype : typeMirrors) {
            if (supertype instanceof DeclaredType) {
                DeclaredType declaredType = (DeclaredType) supertype;
                objects.add(declaredType.asElement());
            }
        }
        return objects;
    }
    
    static void fillFields(ClassWrapper classWrapper) {
        final List<FieldWrapper> fieldWrappers = classWrapper.getAnnotatedElement()
                .getEnclosedElements()
                .stream()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .map(FieldWrapper::new)
                .collect(Collectors.toList());
        classWrapper.setEnclosedFields(fieldWrappers);
    }
    
    static void fillFromInheritedClasses(TypeElement annotatedElement, ClassWrapper classWrapper) {
        final List<Element> objects = lookForSuperClasses(annotatedElement.asType());
        
        final Set<ClassWrapper> wrappers = new LinkedHashSet<>();
        for (Element element : objects) {
            final ClassWrapper anotherWrapper = new ClassWrapper(element, classWrapper.getExportType());
            fillFields(anotherWrapper);
            fillMethods(anotherWrapper);
            assignAnnotations(anotherWrapper);
            FormatterUtils.lookForFormatters(anotherWrapper);
            wrappers.add(anotherWrapper);
        }
        getInheritedMethods(classWrapper, wrappers);
    }
    
    static FieldMethodPair lookForMethodPair(Set<FieldMethodPair> fieldMethodPairs, MethodWrapper aMethod,
                                             ToColumn annotation) {
        FieldMethodPair methodPair = new FieldMethodPair(aMethod.getName());
        methodPair.setStaticMethod(aMethod.isStaticMethod());
        
        if (ToColumn.DEFAULT_NAME.equals(annotation.name())) {
            methodPair.setHeaderName(aMethod.getName());
        } else {
            methodPair.setHeaderName(annotation.name());
        }
        methodPair.setOrder(annotation.value());
        if (aMethod.getFormatter() != null) {
            methodPair.setFormatter(aMethod.getFormatter());
        }
        
        fieldMethodPairs.add(methodPair);
        return methodPair;
    }
    
    static void lookForMethodPair(ToColumn annotation, String possibleFieldNameForMethod,
                                  FieldMethodPair fieldMethodPair) {
        if (annotation != null) {
            fieldMethodPair.setOrder(annotation.value());
            if (ToColumn.DEFAULT_NAME.equals(annotation.name())) {
                fieldMethodPair.setHeaderName(possibleFieldNameForMethod);
            } else {
                fieldMethodPair.setHeaderName(annotation.name());
            }
        }
    }
    
    private static VelocityWrapper commonHandler(ClassWrapper classWrapper) {
        VelocityWrapper wrapper = new VelocityWrapper();
        wrapper.setUseFQNs(classWrapper.isUseFQNs());
        wrapper.setWithIoC(classWrapper.isWithIoc());
        wrapper.setWithHeaders(!classWrapper.isIgnoreHeaders());
        String packageName = "";
        String aPackage = classWrapper.getFQNOfGeneratedClass();
        if (!aPackage.isEmpty()) {
            int index = aPackage.lastIndexOf('.');
            if (index > 0) {
                packageName = aPackage.substring(0, index);
            }
        }
        wrapper.setAPackage(packageName);
        return wrapper;
    }
    
    static boolean lookForInnerMethods(Set<FieldMethodPair> fieldMethodPairs,
                                       MethodWrapper parentMethod,
                                       FieldMethodPair fieldMethodPair) {
        int countOfInnerMethods = 0;
        ExecutableElement executableElement = (ExecutableElement) parentMethod.wrappedElement;
        TypeMirror returnType = executableElement.getReturnType();
        
        if (returnType instanceof DeclaredType) {
            DeclaredType declaredType = (DeclaredType) returnType;
            Element innerElement = declaredType.asElement();
            ObjectToColumns objectToColumns = executableElement.getAnnotation(ObjectToColumns.class);
            if (objectToColumns == null) return false;
            
            ClassWrapper innerWrapper = new ClassWrapper(innerElement, ExportType.NONE);
            fillFields(innerWrapper);
            fillMethods(innerWrapper);
            List<MethodWrapper> methodWrappers = innerWrapper.getEnclosedMethods();
            for (MethodWrapper methodWrapper : methodWrappers) {
                countOfInnerMethods++;
                String parentMethodHeaderName = parentMethod.getName() + "()." + methodWrapper.getName();
                FieldMethodPair innerMethodPair = new FieldMethodPair(methodWrapper.getName(),
                        parentMethodHeaderName);
                innerMethodPair.setOrder(fieldMethodPair.getOrder());
                ToColumn toColumn = methodWrapper.getAnnotation(ToColumn.class);
                String innerMethodHeaderName = "";
                
                if (toColumn == null) {
                    for (FieldWrapper enclosedField : innerWrapper.getEnclosedFields()) {
                        if (enclosedField.getName().equals(methodWrapper.getPossibleFieldName())) {
                            ToColumn annotationOnField = enclosedField.getAnnotation(ToColumn.class);
                            if (annotationOnField == null || annotationOnField.name().equals(ToColumn.DEFAULT_NAME)) {
                                innerMethodHeaderName = enclosedField.getName();
                                break;
                            } else {
                                innerMethodHeaderName = annotationOnField.name();
                            }
                        }
                    }
                } else if (toColumn.name().equals(ToColumn.DEFAULT_NAME)) {
                    innerMethodHeaderName = methodWrapper.getName();
                } else {
                    innerMethodHeaderName = toColumn.name();
                }
                if (objectToColumns.name().equals(ObjectToColumns.DEFAULT_NAME)) {
                    innerMethodPair.setHeaderName(parentMethodHeaderName + "['" + innerMethodHeaderName + "']");
                } else {
                    innerMethodPair.setHeaderName(objectToColumns.name() + "['" + innerMethodHeaderName + "']");
                }
                fieldMethodPairs.add(innerMethodPair);
            }
        }
        return countOfInnerMethods > 0;
    }
    
    static void assignOrder(Set<FieldMethodPair> fieldMethodPairs) {
        int order = 0;
        for (FieldMethodPair fieldMethodPair : fieldMethodPairs) {
            if (!fieldMethodPair.isOrdered()) {
                fieldMethodPair.setOrder(order);
            }
            order++;
        }
    }
    
    public static Collection<FieldMethodPair> getFieldMethodPairs(ClassWrapper classWrapper) {
        Set<FieldMethodPair> fieldMethodPairs = new FieldMethodSet();
        classWrapper.getEnclosedMethods().stream()
                .filter(MethodWrapper::isValid)
                .forEach(aMethod -> {
                    ToColumn toColumn = aMethod.getAnnotation(ToColumn.class);
                    String possibleFieldName = aMethod.getPossibleFieldName();
                    FieldMethodPair fieldMethodPair = null;
                    if (toColumn != null && possibleFieldName != null) {
                        fieldMethodPair = lookForPairWithField(aMethod, toColumn, possibleFieldName);
                    } else if (toColumn != null) {
                        fieldMethodPair = lookForMethodPair(fieldMethodPairs, aMethod, toColumn);
                    }
                    
                    ObjectToColumns objectToColumns = aMethod.getAnnotation(ObjectToColumns.class);
                    boolean innerElementsFound = false;
                    if (objectToColumns != null) {
                        int order = objectToColumns.value();
                        FieldMethodPair parentMethod = new FieldMethodPair(aMethod.getName());
                        parentMethod.setOrder(order);
                        innerElementsFound = lookForInnerMethods(fieldMethodPairs, aMethod, parentMethod);
                    }
                    if (!innerElementsFound && fieldMethodPair != null) {
                        fieldMethodPairs.add(fieldMethodPair);
                    }
                });
        
        assignOrder(fieldMethodPairs);
        fieldMethodPairs.addAll(classWrapper.getInheritedMembers());
        return fieldMethodPairs;
    }
    
    public static FieldMethodPair lookForPairWithField(MethodWrapper aMethod, ToColumn toColumn,
                                                       String possibleFieldName) {
        FieldMethodPair fieldMethodPair;
        fieldMethodPair = new FieldMethodPair(possibleFieldName,
                aMethod.getName());
        fieldMethodPair.setStaticMethod(aMethod.isStaticMethod());
        lookForMethodPair(toColumn, possibleFieldName, fieldMethodPair);
        if (aMethod.getFormatter() != null) {
            fieldMethodPair.setFormatter(aMethod.getFormatter());
        }
        return fieldMethodPair;
    }
    
    public static VelocityWrapper getVelocityWrapper(ClassWrapper classWrapper) {
        ExportType export = classWrapper.getExportType();
        VelocityWrapper wrapper = null;
        if (export == ExportType.EXCEL || export == ExportType.CSV) {
            wrapper = WrapperUtils.commonHandler(classWrapper);
        }
        if (wrapper != null && classWrapper.getAnnotatedElement() instanceof TypeElement) {
            Name qualifiedName = ((TypeElement) classWrapper.getAnnotatedElement()).getQualifiedName();
            wrapper.setClassName(qualifiedName.toString());
            wrapper.setSimplifiedClassName(classWrapper.getAnnotatedElement().getSimpleName().toString());
            Collection<FieldMethodPair> correspondanceFieldMethod = WrapperUtils.getFieldMethodPairs(classWrapper);
            
            wrapper.setCorrespondanceFieldMethod(correspondanceFieldMethod);
        }
        return wrapper;
    }
    
    protected static String capitalize(String correspondantFieldName) {
        char c = correspondantFieldName.charAt(0);
        c = Character.toUpperCase(c);
        return c + correspondantFieldName.substring(1);
        
    }
    
    protected static String uncapitalize(String correspondantFieldName) {
        char c = correspondantFieldName.charAt(0);
        c = Character.toLowerCase(c);
        return c + correspondantFieldName.substring(1);
        
    }
    
    public static void fillLombokAnnotations(ClassWrapper classWrapper) {
        
        boolean withLombok = false;
        for (AnnotationMirror annotationMirror : classWrapper.getAnnotatedElement().getAnnotationMirrors()) {
            if (annotationMirror.getAnnotationType().toString().equals("lombok.Getter")) {
                withLombok = true;
                break;
            }
        }
        
        classWrapper.setWithLombok(withLombok);
    }
}
