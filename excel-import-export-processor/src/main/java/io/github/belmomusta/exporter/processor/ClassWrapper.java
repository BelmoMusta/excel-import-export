package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.api.annotation.CSV;
import io.github.belmomusta.exporter.api.annotation.Excel;
import io.github.belmomusta.exporter.api.annotation.ExcelFormat;
import io.github.belmomusta.exporter.api.annotation.ToColumn;
import io.github.belmomusta.exporter.processor.velocity.CSVVelocityWrapper;
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
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
		return common(annotatedElement, ".excel.export.", "ExcelExporter");
	}
	
	public static ClassWrapper ofCSV(Element annotatedElement) {
		return common(annotatedElement, ".csv.export.", "CSVExporter");
	}
	
	private static ClassWrapper common(Element annotatedElement, String packagePrefix, String classSuffix) {
		String generatedCSVClassName = annotatedElement.getSimpleName().toString() + classSuffix;
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
		String packageName = aPackage.substring(0, index);
		classWrapper.fQNOfGeneratedClass = packageName + packagePrefix + generatedCSVClassName;
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
		lookForFormatters(classWrapper);
		return classWrapper;
	}
	
	private static void lookForFormatters(ClassWrapper classWrapper) {
		Element formatAnnotation =
				ExportProcessor.processingEnvironment.getElementUtils().getTypeElement(ExcelFormat.class.getName());
		for (MethodWrapper method : classWrapper.enclosedMethods) {
			boolean formatterFound = false;
			outerLoop:
			for (AnnotationMirror annotationMirror : method.wrappedElement.getAnnotationMirrors()) {
				if (annotationMirror.getAnnotationType().equals(formatAnnotation.asType())) {
					Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues =
							annotationMirror.getElementValues();
					for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
							elementValues.entrySet()) {
						ExecutableElement key = entry.getKey();
						AnnotationValue value = entry.getValue();
						
						if (key.toString().equals("formatter()")) {
							method.setFormatter(value.getValue().toString());
							formatterFound = true;
							break outerLoop;
						}
					}
					break;
				}
			}
			if(!formatterFound){
				for (FieldWrapper enclosedField : classWrapper.enclosedFields) {
					String possibleFieldName = method.getPossibleFieldName();
					if(enclosedField.getName().equals(possibleFieldName)){
						outerLoop : for (AnnotationMirror annotationMirror : enclosedField.wrappedElement.getAnnotationMirrors()) {
							if (annotationMirror.getAnnotationType().equals(formatAnnotation.asType())) {
								Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues =
										annotationMirror.getElementValues();
								for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
										elementValues.entrySet()) {
									ExecutableElement key = entry.getKey();
									AnnotationValue value = entry.getValue();
									
									if (key.toString().equals("formatter()")) {
										method.setFormatter(value.getValue().toString());
										break outerLoop;
									}
								}
								break;
							}
						}
						
					}
					
				}
			}
		}
		
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
					if (possibleFieldNameForMethod != null) {
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
		return fieldMethodPairs;
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
		wrapper = new CSVVelocityWrapper();
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
