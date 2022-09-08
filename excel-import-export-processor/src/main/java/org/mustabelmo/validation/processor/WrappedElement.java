package org.mustabelmo.validation.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class WrappedElement {
	final Element element;
	private List<Class<? extends Annotation>> processedAnnotations = new ArrayList<>();
	private List<Annotation> annotations = new ArrayList<>();
	
	public WrappedElement(Element method) {
		this.element = method;
	}
	
	public void setProcessed(Class<? extends Annotation> processed) {
		this.processedAnnotations.add(processed);
	}
	
	public boolean isProcessed(Class<? extends Annotation> annotation) {
		return processedAnnotations.contains(annotation);
	}
	
	public String getName() {
		return element.getSimpleName().toString();
	}
	
	public boolean isMethod() {
		return element.getKind() == ElementKind.METHOD;
	}
	
	public boolean isField() {
		return element.getKind() == ElementKind.FIELD;
	}
	
	public boolean isValid() {
		if (isMethod()) {
			final ExecutableElement executableMethod = (ExecutableElement) element;
			final TypeKind kind = getKind();
			return kind != TypeKind.VOID
					&& !executableMethod.getModifiers().contains(Modifier.ABSTRACT)
					&& !executableMethod.getModifiers().contains(Modifier.PRIVATE);
		}
		return false;
	}
	
	public TypeKind getKind() {
		ExecutableElement executableMethod = (ExecutableElement) element;
		return executableMethod.getReturnType().getKind();
	}
	
	public String getPossibleFieldNameForMethod() {
		String correspondantFieldName = element.getSimpleName().toString();
		if (correspondantFieldName.startsWith("is") && correspondantFieldName.length() > 2) {
			correspondantFieldName = correspondantFieldName.substring(2);
		} else if (correspondantFieldName.startsWith("get") && correspondantFieldName.length() > 3) {
			correspondantFieldName = correspondantFieldName.substring(3);
		}
		
		correspondantFieldName = uncapitalize(correspondantFieldName);
		return correspondantFieldName;
	}
	
	private String uncapitalize(String correspondantFieldName) {
		char c = correspondantFieldName.charAt(0);
		c = Character.toLowerCase(c);
		return c + correspondantFieldName.substring(1);
		
	}
	
	public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
		for (Annotation anno : annotations) {
			if (anno.annotationType() == annotationType) {
				return (A) anno;
			}
		}
		return element.getAnnotation(annotationType);
	}
	
	public boolean add(Annotation annotation) {
		return annotations.add(annotation);
	}
	
}
