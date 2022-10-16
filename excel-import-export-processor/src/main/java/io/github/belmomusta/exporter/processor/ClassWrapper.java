package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.api.annotation.CSV;
import io.github.belmomusta.exporter.api.annotation.Excel;
import io.github.belmomusta.exporter.api.annotation.SpringComponent;
import io.github.belmomusta.exporter.processor.types.FieldMethodSet;
import io.github.belmomusta.exporter.processor.velocity.FieldMethodPair;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ClassWrapper {
	
	private final Element annotatedElement;
	private List<MethodWrapper> enclosedMethods;
	private List<FieldWrapper> enclosedFields;
	private String fQNOfGeneratedClass;
	private Set<FieldMethodPair> inheritedMembers ;
	private ExcelCsvProperties properties;
	private ExportType exportType;
	
	 ClassWrapper(Element annotatedElement, ExportType exportType) {
		this.annotatedElement = annotatedElement;
		 inheritedMembers = new FieldMethodSet();
		 enclosedMethods = new ArrayList<>();
		 enclosedFields = new ArrayList<>();
		 this.exportType = exportType;
		 properties = new ExcelCsvProperties(this);
		 
	}
	
	
	public static ClassWrapper of(Element annotatedElement) {
		return common(annotatedElement, ExportType.EXCEL);
	}
	
	public static ClassWrapper ofCSV(Element annotatedElement) {
		return common(annotatedElement, ExportType.CSV);
	}
	
	private static ClassWrapper common(Element annotatedElement, ExportType exportType) {
		final ClassWrapper classWrapper = new ClassWrapper(annotatedElement, exportType);
		WrapperUtils.fillMethods(classWrapper);
		WrapperUtils.fillFields(classWrapper);
		WrapperUtils.fillFQN(classWrapper, exportType.getPackagePrefix(), exportType.getClassSuffix());
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
		boolean withIoC;
		
		public ExcelCsvProperties(ClassWrapper classWrapper) {
			if(classWrapper.getExportType() == ExportType.EXCEL){
				
				Excel excel = classWrapper.getAnnotation(Excel.class);
				if (excel != null) {
					useFQN = excel.useFQNs();
					ignoreHeaders = excel.ignoreHeaders();
				}
			} else {
				CSV csv = classWrapper.getAnnotation(CSV.class);
				if (csv != null) {
					useFQN = csv.useFQNs();
					ignoreHeaders = csv.ignoreHeaders();
				}
			}
			
			SpringComponent springComponent = classWrapper.getAnnotation(SpringComponent.class);
			if(springComponent != null){
				
				withIoC = true;
			}
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ClassWrapper that = (ClassWrapper) o;
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