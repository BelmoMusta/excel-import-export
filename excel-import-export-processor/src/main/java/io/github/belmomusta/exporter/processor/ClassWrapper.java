package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.api.annotation.CSV;
import io.github.belmomusta.exporter.api.annotation.Excel;
import io.github.belmomusta.exporter.api.annotation.ToColumn;
import io.github.belmomusta.exporter.processor.types.FieldMethodSet;
import io.github.belmomusta.exporter.processor.velocity.FieldMethodPair;
import io.github.belmomusta.exporter.processor.velocity.VelocityWrapper;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ClassWrapper {
	
	private final Element annotatedElement;
	private List<MethodWrapper> enclosedMethods;
	private List<FieldWrapper> enclosedFields;
	private String fQNOfGeneratedClass;
	
	Set<FieldMethodPair> inheritedMembers ;
	
	 ClassWrapper(Element annotatedElement) {
		this.annotatedElement = annotatedElement;
		 inheritedMembers = new FieldMethodSet();
		 enclosedMethods = new ArrayList<>();
		 enclosedFields = new ArrayList<>();
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
		WrapperUtils.lookForFormatters(classWrapper);
		WrapperUtils.fillFromInheritedClasses((TypeElement) annotatedElement, classWrapper);
		return classWrapper;
	}
	
	public Collection<FieldMethodPair> getCorrespondanceFieldMethod() {
		Set<FieldMethodPair> fieldMethodPairs = new FieldMethodSet();
		this.enclosedMethods.stream()
				.filter(MethodWrapper::isValid)
				.forEach(aMethod -> {
					ToColumn annotation = aMethod.getAnnotation(ToColumn.class);
					String possibleFieldNameForMethod = aMethod.getPossibleFieldName();
					if (annotation != null && possibleFieldNameForMethod != null) {
						FieldMethodPair fieldMethodPair = new FieldMethodPair(possibleFieldNameForMethod,
								aMethod.getName());
						fieldMethodPair.setStaticMethod(aMethod.isStaticMethod());
						WrapperUtils.applyHeaders(annotation, possibleFieldNameForMethod, fieldMethodPair);
						fieldMethodPairs.add(fieldMethodPair);
						if (aMethod.getFormatter() != null) {
							fieldMethodPair.setFormatter(aMethod.getFormatter());
						}
					} else if (annotation != null) {
						WrapperUtils.applyHeaders(fieldMethodPairs, aMethod, annotation);
					}
				});
		
		assignOrder(fieldMethodPairs);
		fieldMethodPairs.addAll(inheritedMembers);
		return fieldMethodPairs;
	}
	
	private void assignOrder(Set<FieldMethodPair> fieldMethodPairs) {
		int i = 0;
		for (FieldMethodPair fieldMethodPair : fieldMethodPairs) {
		   if (!fieldMethodPair.isOrdered()) {
			   fieldMethodPair.setOrder(i);
			}
		   i++;
	   }
	}
	
	public String getFQNOfGeneratedClass() {
		return fQNOfGeneratedClass;
	}
	
	public VelocityWrapper getVelocityWrapper() {
		VelocityWrapper wrapper = WrapperUtils.handleExcel(this);
		if (wrapper == null) {
			wrapper = WrapperUtils.handleCSV(this);
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
	
	public  <A extends Annotation> boolean hasAnnotation(Class<A> a) {
		return getAnnotation(a) != null;
	}
	
	public <A extends Annotation> A getAnnotation(Class<A> a) {
		return annotatedElement.getAnnotation(a);
	}
	
	public boolean isUseFQNs() {
		 boolean useFQNS = false;
		 Excel excel = getAnnotation(Excel.class);
		 if(excel != null){
			 useFQNS = excel.useFQNs();
		 } else {
			 CSV csv = getAnnotation(CSV.class);
			 if(csv != null){
				 useFQNS = csv.useFQNs();
			 }
		 }
		 return useFQNS;
	}
	public boolean isIgnoreHeaders() {
		 boolean ignoreHeaders = false;
		 Excel excel = getAnnotation(Excel.class);
		 if(excel != null){
			 ignoreHeaders = excel.ignoreHeaders();
		 } else {
			 CSV csv = getAnnotation(CSV.class);
			 if(csv != null){
				 ignoreHeaders = csv.ignoreHeaders();
			 }
		 }
		 return ignoreHeaders;
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
}