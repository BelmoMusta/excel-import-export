package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.api.annotation.Export;
import io.github.belmomusta.exporter.api.common.ExportType;
import io.github.belmomusta.exporter.processor.types.FieldMethodSet;
import io.github.belmomusta.exporter.processor.velocity.FieldMethodPair;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractClassWrapper {
	
	private final Element annotatedElement;
	private List<MethodWrapper> enclosedMethods;
	private List<FieldWrapper> enclosedFields;
	private String fQNOfGeneratedClass;
	private Set<FieldMethodPair> inheritedMembers ;
	private ExcelCsvProperties properties;
	private ExportType exportType;
	
	 protected AbstractClassWrapper(Element annotatedElement, ExportType exportType) {
		this.annotatedElement = annotatedElement;
		 inheritedMembers = new FieldMethodSet();
		 enclosedMethods = new ArrayList<>();
		 enclosedFields = new ArrayList<>();
		 this.exportType = exportType;
		 properties = new ExcelCsvProperties(this);
	}
	
	public String getFQNOfGeneratedClass() {
		return fQNOfGeneratedClass;
	}
	
	public <A extends Annotation> A getAnnotation(Class<A> a) {
		return annotatedElement.getAnnotation(a);
	}
	
	public boolean isUseFQNs() {
		 return properties.useFQN;
	}
	
	public boolean isIgnoreHeaders() {
		return properties.ignoreHeaders;
	}
	
	public Element getAnnotatedElement() {
		return annotatedElement;
	}
	
	public List<MethodWrapper> getEnclosedMethods() {
		return enclosedMethods;
	}
	
	public List<FieldWrapper> getEnclosedFields() {
		return enclosedFields;
	}
	
	public void setEnclosedMethods(List<MethodWrapper> enclosedMethods) {
		this.enclosedMethods = enclosedMethods;
	}
	
	public void setFQNOfGeneratedClass(String fQNOfGeneratedClass) {
		this.fQNOfGeneratedClass = fQNOfGeneratedClass;
	}
	
	public void setEnclosedFields(List<FieldWrapper> enclosedFields) {
		this.enclosedFields = enclosedFields;
	}
	
	public Set<FieldMethodPair> getInheritedMembers() {
		return inheritedMembers;
	}
	
	protected static class ExcelCsvProperties {
		boolean useFQN;
		boolean ignoreHeaders;
		boolean withIoC;
		
		public ExcelCsvProperties(AbstractClassWrapper classWrapper) {
			Export export = classWrapper.getAnnotation(Export.class);
			if (export == null || export.type().length ==0) {
				return;
			}
			
			useFQN = export.useFQNs();
			ignoreHeaders = export.ignoreHeaders();
			withIoC = export.withIoC();
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AbstractClassWrapper that = (AbstractClassWrapper) o;
		return annotatedElement.equals(that.annotatedElement);
	}
	
	public ExportType getExportType() {
		return exportType;
	}
	
	public boolean isWithIoc() {
		return properties.withIoC;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(annotatedElement);
	}
}