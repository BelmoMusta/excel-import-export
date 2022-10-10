package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.api.annotation.CSV;
import io.github.belmomusta.exporter.api.annotation.Excel;
import io.github.belmomusta.exporter.processor.types.FieldMethodSet;
import io.github.belmomusta.exporter.processor.velocity.FieldMethodPair;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClassWrapper {
	
	private final Element annotatedElement;
	private List<MethodWrapper> enclosedMethods;
	private List<FieldWrapper> enclosedFields;
	private String fQNOfGeneratedClass;
	private Set<FieldMethodPair> inheritedMembers ;
	private ExcelCsvProperties properties;
	
	 ClassWrapper(Element annotatedElement) {
		this.annotatedElement = annotatedElement;
		 inheritedMembers = new FieldMethodSet();
		 enclosedMethods = new ArrayList<>();
		 enclosedFields = new ArrayList<>();
		 properties = new ExcelCsvProperties(this);
		 
	}
	
	public static ClassWrapper of(Element annotatedElement) {
		return common(annotatedElement, ".export.excel.", "ExcelExporter");
	}
	
	public static ClassWrapper ofCSV(Element annotatedElement) {
		return common(annotatedElement, ".export.csv.", "CSVExporter");
	}
	
	private static ClassWrapper common(Element annotatedElement, String packagePrefix, String classSuffix) {
		final ClassWrapper classWrapper = new ClassWrapper(annotatedElement);
		WrapperUtils.fillMethods(classWrapper);
		WrapperUtils.fillFields(classWrapper);
		WrapperUtils.fillFQN(classWrapper, packagePrefix, classSuffix);
		WrapperUtils.assignAnnotations(classWrapper);
		FormatterUtils.lookForFormatters(classWrapper);
		WrapperUtils.fillFromInheritedClasses((TypeElement) annotatedElement, classWrapper);
		return classWrapper;
	}
	
	public String getFQNOfGeneratedClass() {
		return fQNOfGeneratedClass;
	}
	
	public <A extends Annotation> boolean hasAnnotation(Class<A> a) {
		return getAnnotation(a) != null;
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
	
	private static class ExcelCsvProperties {
		boolean useFQN;
		boolean ignoreHeaders;
		
		public ExcelCsvProperties(ClassWrapper classWrapper) {
			Excel excel = classWrapper.getAnnotation(Excel.class);
			if(excel != null) {
				useFQN = excel.useFQNs();
				ignoreHeaders = excel.ignoreHeaders();
			} else {
				CSV csv = classWrapper.getAnnotation(CSV.class);
				if(csv != null){
					useFQN = csv.useFQNs();
					ignoreHeaders = csv.ignoreHeaders();
				}
			}
		}
	}
}