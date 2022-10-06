package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.api.annotation.CSV;
import io.github.belmomusta.exporter.api.annotation.Excel;
import io.github.belmomusta.exporter.api.annotation.ColumnFormat;
import io.github.belmomusta.exporter.api.annotation.ToColumn;
import io.github.belmomusta.exporter.processor.velocity.ExcelVelocityWrapper;
import io.github.belmomusta.exporter.processor.velocity.FieldMethodPair;
import io.github.belmomusta.exporter.processor.velocity.VelocityWrapper;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ClassWrapper {
	
	private Element annotatedElement;
	private List<MethodWrapper> enclosedMethods = new ArrayList<>();
	private List<FieldWrapper> enclosedFields = new ArrayList<>();
	private String fQNOfGeneratedClass;
	
	Set<FieldMethodPair> inheditedPublicMethods = new TreeSet<>((a, b) -> {
		if(a.getOrder() == b.getOrder()){
			return 1;
		}
		return a.getOrder() - b.getOrder();
	});
	
	private ClassWrapper(Element annotatedElement) {
		this.annotatedElement = annotatedElement;
	}
	
	public static ClassWrapper of(Element annotatedElement) {
		return common(annotatedElement, ".export.excel.", "ExcelExporter");
	}
	
	public static ClassWrapper ofCSV(Element annotatedElement) {
		return common(annotatedElement, ".export.csv.", "CSVExporter");
	}
	
	private static ClassWrapper common(Element annotatedElement, String packagePrefix, String classSuffix) {
		ClassWrapper classWrapper = new ClassWrapper(annotatedElement);
		fillMethods(classWrapper);
		fillFields(classWrapper);
		String generatedClassName = calculateGeneratedClassName(classSuffix, classWrapper);
		String packageName = calculatePackageName((TypeElement) classWrapper.annotatedElement);
		classWrapper.fQNOfGeneratedClass = packageName + packagePrefix + generatedClassName;
		assignAnnotations(classWrapper);
		lookForFormatters(classWrapper);
		fillFromInheditedClasses((TypeElement) annotatedElement, classWrapper);
		return classWrapper;
	}
	
	private static void fillFromInheditedClasses(TypeElement annotatedElement, ClassWrapper classWrapper) {
		List<Element> objects = new ArrayList<>();
		TypeMirror superclass = annotatedElement.getSuperclass();
		
		while(superclass != null){
			superclass.accept(new MTypeVisitor(), objects);
			TypeMirror superclass1 = superclass.accept(new InheritanceTreeTypeVisitor(), null, null);
			
		}
		
		List<ClassWrapper> wrappers = new ArrayList<>();
		for (Element object : objects) {
			ClassWrapper anotherWrapper = new ClassWrapper(object);
			fillMethods(anotherWrapper);
 			assignAnnotations(anotherWrapper);
			lookForFormatters(anotherWrapper);
			wrappers.add(anotherWrapper);
		}
		getInheritedMethods(classWrapper, wrappers);
	}
	
	private static void getInheritedMethods(ClassWrapper classWrapper, List<ClassWrapper> wrappers) {
		for (ClassWrapper wrapper : wrappers) {
			classWrapper.addFieldMethodPair(wrapper.getCorrespondanceFieldMethod());
		}
	}
	
	private static String calculateGeneratedClassName(String classSuffix, ClassWrapper classWrapper) {
		return classWrapper.annotatedElement.getSimpleName() + classSuffix;
	}
	
	private static void fillFields(ClassWrapper classWrapper) {
		classWrapper.enclosedFields = classWrapper.annotatedElement.getEnclosedElements().stream()
				.filter(e -> e.getKind() == ElementKind.FIELD)
				.map(FieldWrapper::new)
				.collect(Collectors.toList());
	}
	
	private static void fillMethods(ClassWrapper classWrapper) {
		classWrapper.enclosedMethods = classWrapper.annotatedElement.getEnclosedElements().stream()
				.filter(e -> e.getKind() == ElementKind.METHOD)
				.map(MethodWrapper::new)
				.collect(Collectors.toList());
	}
	
	private static String calculatePackageName(TypeElement annotatedElement) {
		Name qualifiedName = annotatedElement.getQualifiedName();
		String aPackage = qualifiedName.toString();
		int index = aPackage.lastIndexOf('.');
		return aPackage.substring(0, index);
	}
	
	private static void assignAnnotations(ClassWrapper classWrapper) {
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
	}
	
	private static void lookForFormatters(ClassWrapper classWrapper) {
		Element formatAnnotation = ExportProcessor.processingEnvironment.getElementUtils().getTypeElement(ColumnFormat.class.getName());
		for (MethodWrapper method : classWrapper.enclosedMethods) {
			boolean formatterFound = tryToFindFormatters(formatAnnotation, method, method.wrappedElement.getAnnotationMirrors());
			
			if(!formatterFound){
				for (FieldWrapper enclosedField : classWrapper.enclosedFields) {
					String possibleFieldName = method.getPossibleFieldName();
					if(enclosedField.getName().equals(possibleFieldName)) {
						tryToFindFormatters(formatAnnotation, method, enclosedField.wrappedElement.getAnnotationMirrors());
						
					}
					
				}
			}
		}
		
	}
	
	private static boolean tryToFindFormatters(Element formatAnnotation, MethodWrapper method, List<?
			extends AnnotationMirror> mirrors) {
		boolean formatterFound = false;
		for (AnnotationMirror annotationMirror : mirrors) {
			if (annotationMirror.getAnnotationType().equals(formatAnnotation.asType())) {
				Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues =
						annotationMirror.getElementValues();
				for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
						elementValues.entrySet()) {
					if (formatterFound) {
						break;
					}
					
					ExecutableElement key = entry.getKey();
					AnnotationValue value = entry.getValue();
					
					if (key.toString().equals("formatter()")) {
						method.setFormatter(value.getValue().toString());
						formatterFound = true;
					}
				}
				if(formatterFound){
					break;
				}
			}
		}
		return formatterFound;
	}
	
	private Annotation lookForAnnotationInPossibleField(MethodWrapper methodWrapper) {
		String possibleFieldName = methodWrapper.getPossibleFieldName();
		for (FieldWrapper enclosedField : this.enclosedFields) {
			if (enclosedField.getName().equals(possibleFieldName)) {
				return enclosedField.getAnnotation(ToColumn.class);
			}
		}
		return null;
	}
	
	public Collection<FieldMethodPair> getCorrespondanceFieldMethod() {
		Set<FieldMethodPair> fieldMethodPairs = new TreeSet<>();
		this.enclosedMethods.stream()
				.filter(MethodWrapper::isValid)
				.forEach(aMethod -> {
					ToColumn annotation = aMethod.getAnnotation(ToColumn.class);
					String possibleFieldNameForMethod = aMethod.getPossibleFieldName();
					if (annotation != null && possibleFieldNameForMethod != null) {
						FieldMethodPair fieldMethodPair = new FieldMethodPair(possibleFieldNameForMethod,
								aMethod.getName());
						applyHeaders(annotation, possibleFieldNameForMethod, fieldMethodPair);
						fieldMethodPairs.add(fieldMethodPair);
						if (aMethod.getFormatter() != null) {
							fieldMethodPair.setFormatter(aMethod.getFormatter());
						}
					} else if (annotation != null) {
						applyHeaders(fieldMethodPairs, aMethod, annotation);
					}
					
				});
		
		int i = 0;
		Set<FieldMethodPair> set = new TreeSet<>((a, b) -> {
			if(a.getOrder() == b.getOrder()){
				return 1;
			}
			return a.getOrder() - b.getOrder();
		});
		for (FieldMethodPair fieldMethodPair : fieldMethodPairs) {
			if (!fieldMethodPair.isOrdered()) {
				fieldMethodPair.setOrder(i++);
 			} else {
 				i = i + 1;
			}
		}
		
		set.addAll(fieldMethodPairs);
		set.addAll(inheditedPublicMethods);
		return new ArrayList<>(set);
	}
	public void addFieldMethodPair(Collection<FieldMethodPair> fieldMethodPair){
		inheditedPublicMethods.addAll(fieldMethodPair);
	}
	
	private void applyHeaders(Set<FieldMethodPair> fieldMethodPairs, MethodWrapper aMethod, ToColumn annotation) {
		FieldMethodPair methodPair = new FieldMethodPair(aMethod.getName());
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
	}
	
	private void applyHeaders(ToColumn annotation, String possibleFieldNameForMethod, FieldMethodPair fieldMethodPair) {
		if (annotation != null) {
			fieldMethodPair.setOrder(annotation.value());
			if (ToColumn.DEFAULT_NAME.equals(annotation.name())) {
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
		VelocityWrapper wrapper = null;
		Excel excel = annotatedElement.getAnnotation(Excel.class);
		if (excel != null) {
			wrapper = handleExcel(excel);
		} else {
			CSV csv = annotatedElement.getAnnotation(CSV.class);
			if (csv != null) {
				wrapper = handleCSV(csv);
			}
		}
		if (wrapper != null && annotatedElement instanceof TypeElement) {
			Name qualifiedName = ((TypeElement) annotatedElement).getQualifiedName();
			wrapper.setClassName(qualifiedName.toString());
			wrapper.setSimplifiedClassName(annotatedElement.getSimpleName().toString());
			Collection<FieldMethodPair> correspondanceFieldMethod = getCorrespondanceFieldMethod();
			
			wrapper.setCorrespondanceFieldMethod(correspondanceFieldMethod);
		}
		return wrapper;
	}
	
	private VelocityWrapper handleCSV(CSV csv) {
		VelocityWrapper wrapper;
		wrapper = new VelocityWrapper();
		wrapper.setUseFQNs(csv.useFQNs());
		if (!csv.ignoreHeaders()) {
			wrapper.setWithHeaders(true);
		}
		String csvPackagee = getFQNOfGeneratedClass();
		int index = csvPackagee.lastIndexOf('.');
		wrapper.setAPackage(csvPackagee.substring(0, index));
		return wrapper;
	}
	
	private VelocityWrapper handleExcel(Excel excel) {
		VelocityWrapper wrapper;
		wrapper = new ExcelVelocityWrapper();
		wrapper.setUseFQNs(excel.useFQNs());
		if (!excel.ignoreHeaders()) {
			wrapper.setWithHeaders(true);
		}
		String aPackage = getFQNOfGeneratedClass();
		int index = aPackage.lastIndexOf('.');
		String packageName = aPackage.substring(0, index);
		wrapper.setAPackage(packageName);
		return wrapper;
	}
}
