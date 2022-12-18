package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.api.common.ExportType;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class ClassWrapper extends AbstractClassWrapper{
	
	 ClassWrapper(Element annotatedElement, ExportType exportType) {
		 super(annotatedElement, exportType);
	}
	
	public static ClassWrapper of(Element annotatedElement, ExportType exportType) {
		final ClassWrapper classWrapper = new ClassWrapper(annotatedElement, exportType);
		WrapperUtils.fillLombokAnnotations(classWrapper);
		WrapperUtils.fillFields(classWrapper);
		WrapperUtils.fillMethods(classWrapper);
		WrapperUtils.fillFQN(classWrapper, exportType.getPackagePrefix(), exportType.getClassSuffix());
		WrapperUtils.assignAnnotations(classWrapper);
		FormatterUtils.lookForFormatters(classWrapper);
		WrapperUtils.fillFromInheritedClasses((TypeElement) annotatedElement, classWrapper);
		return classWrapper;
	}
	
}