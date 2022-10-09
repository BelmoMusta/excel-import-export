package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.api.annotation.CSV;
import io.github.belmomusta.exporter.api.annotation.ColumnFormat;
import io.github.belmomusta.exporter.api.annotation.Excel;
import io.github.belmomusta.exporter.api.annotation.ToColumn;
import io.github.belmomusta.exporter.processor.velocity.FieldMethodPair;
import io.github.belmomusta.exporter.processor.velocity.VelocityWrapper;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WrapperUtils {
	static boolean tryToFindFormatters(Element formatAnnotation, MethodWrapper method, List<?
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
					formatterFound = isFormatterFound(method, entry);
				}
				if(formatterFound){
					break;
				}
			}
		}
		return formatterFound;
	}
	
	private static boolean isFormatterFound(MethodWrapper method,  Map.Entry<?
			extends ExecutableElement, ? extends AnnotationValue> entry) {
		ExecutableElement key = entry.getKey();
		AnnotationValue value = entry.getValue();
		boolean formatterFound = false;
		
		if (key.toString().equals("formatter()")) {
			method.setFormatter(value.getValue().toString());
			formatterFound = true;
		}
		return formatterFound;
	}
	
	static void lookForFormatters(ClassWrapper classWrapper) {
		Element formatAnnotation = ExportProcessor.processingEnvironment.getElementUtils().getTypeElement(ColumnFormat.class.getName());
		for (MethodWrapper method : classWrapper.getEnclosedMethods()) {
			boolean formatterFound = tryToFindFormatters(formatAnnotation, method, method.wrappedElement.getAnnotationMirrors());
			
			if (!formatterFound) {
				for (FieldWrapper enclosedField : classWrapper.getEnclosedFields()) {
					String possibleFieldName = method.getPossibleFieldName();
					if(enclosedField.getName().equals(possibleFieldName)) {
						tryToFindFormatters(formatAnnotation, method, enclosedField.wrappedElement.getAnnotationMirrors());
					}
				}
			}
		}
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
		Element enclosingElement = annotatedElement;
		do {
			enclosingElement = enclosingElement.getEnclosingElement();
			
		} while (!(enclosingElement instanceof PackageElement));
		
		PackageElement packageElement = (PackageElement) enclosingElement;
		Name qualifiedName = packageElement.getQualifiedName();
		return qualifiedName.toString();
	}
	
	static void fillMethods(ClassWrapper classWrapper) {
		Predicate<Element> predicate = e -> e.getKind() == ElementKind.METHOD;
		Function<Element, MethodWrapper> mapper = MethodWrapper::new;
		
		final List<MethodWrapper> methodWrappers = classWrapper.getAnnotatedElement()
				.getEnclosedElements().stream()
				.filter(predicate)
				.map(mapper)
				.collect(Collectors.toList());
		classWrapper.setEnclosedMethods(methodWrappers);
	}
	
	static void fillFQN(ClassWrapper classWrapper, String packagePrefix, String classSuffix) {
		String generatedClassName = calculateGeneratedClassName(classSuffix, classWrapper);
		String packageName = calculatePackageName((TypeElement) classWrapper.getAnnotatedElement());
		if(packageName.isEmpty()){
			classWrapper.setFQNOfGeneratedClass(generatedClassName);
		} else {
			classWrapper.setFQNOfGeneratedClass(packageName + packagePrefix + generatedClassName);
		}
	}
	
	private static String calculateGeneratedClassName(String classSuffix, ClassWrapper classWrapper) {
		return classWrapper.getAnnotatedElement().getSimpleName() + classSuffix;
	}
	
	static void getInheritedMethods(ClassWrapper classWrapper, List<ClassWrapper> wrappers) {
		for (ClassWrapper wrapper : wrappers) {
			Collection<FieldMethodPair> correspondanceFieldMethod = wrapper.getCorrespondanceFieldMethod();
			Collection<FieldMethodPair> inheritedMetods;
			if(wrapper.getAnnotatedElement().getKind() == ElementKind.INTERFACE){
				//  do not take static methods if it is an interface
				inheritedMetods = correspondanceFieldMethod.stream()
						.filter(m -> !m.isStaticMethod())
						.collect(Collectors.toList());
			} else {
				inheritedMetods = correspondanceFieldMethod;
			}
			addInheritedFieldMethodPairs(classWrapper, inheritedMetods);
		}
	}
	
	public static void addInheritedFieldMethodPairs(ClassWrapper classWrapper, Collection<FieldMethodPair> fieldMethodPair){
		classWrapper.inheritedMembers.addAll(fieldMethodPair);
	}
	
	static void lookForSuperClasses(TypeMirror annotatedElement, List<Element> objects) {
		List<? extends TypeMirror> typeMirrors = ExportProcessor.processingEnvironment.getTypeUtils().directSupertypes(annotatedElement);
		for (TypeMirror supertype : typeMirrors) {
			supertype.accept(new MTypeVisitor(), objects);
			lookForSuperClasses(supertype, objects);
		}
	}
	
	static void fillFields(ClassWrapper classWrapper) {
		final Predicate<Element> predicate = e -> e.getKind() == ElementKind.FIELD;
		final Function<Element, FieldWrapper> mapper = FieldWrapper::new;
		final List<FieldWrapper> fieldWrappers = classWrapper.getAnnotatedElement()
				.getEnclosedElements()
				.stream()
				.filter(predicate)
				.map(mapper)
				.collect(Collectors.toList());
		classWrapper.setEnclosedFields(fieldWrappers);
	}
	
	static void fillFromInheritedClasses(TypeElement annotatedElement, ClassWrapper classWrapper) {
		final List<Element> objects = new ArrayList<>();
		lookForSuperClasses(annotatedElement.asType(), objects);
		
		final List<ClassWrapper> wrappers = new ArrayList<>();
		for (Element element : objects) {
			final ClassWrapper anotherWrapper = new ClassWrapper(element);
			fillMethods(anotherWrapper);
 			assignAnnotations(anotherWrapper);
			lookForFormatters(anotherWrapper);
			wrappers.add(anotherWrapper);
		}
		getInheritedMethods(classWrapper, wrappers);
	}
	
	static void applyHeaders(Set<FieldMethodPair> fieldMethodPairs, MethodWrapper aMethod, ToColumn annotation) {
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
	}
	
	static void applyHeaders(ToColumn annotation, String possibleFieldNameForMethod, FieldMethodPair fieldMethodPair) {
		if (annotation != null) {
			fieldMethodPair.setOrder(annotation.value());
			if (ToColumn.DEFAULT_NAME.equals(annotation.name())) {
				fieldMethodPair.setHeaderName(possibleFieldNameForMethod);
			} else {
				fieldMethodPair.setHeaderName(annotation.name());
			}
		}
	}
	
	static VelocityWrapper handleExcel(ClassWrapper classWrapper) {
		if (classWrapper.hasAnnotation(Excel.class)) {
			return commonHandler(classWrapper);
		}
		return null;
	}
	
	private static VelocityWrapper commonHandler(ClassWrapper classWrapper) {
		VelocityWrapper wrapper = new VelocityWrapper();
		wrapper.setUseFQNs(classWrapper.isUseFQNs());
		wrapper.setWithHeaders(!classWrapper.isIgnoreHeaders());
		String packageName = "" ;
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
	
	static VelocityWrapper handleCSV(ClassWrapper classWrapper) {
		if (classWrapper.hasAnnotation(CSV.class)) {
			return commonHandler(classWrapper);
		}
		return null;
	}
}
