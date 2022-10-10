package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.api.annotation.ColumnFormat;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.List;
import java.util.Map;

public class FormatterUtils {
	private FormatterUtils(){
		throw new IllegalStateException("not to be instantiated");
	}
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
}
