package io.github.belmomusta.exporter.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes({
		"io.github.belmomusta.exporter.api.annotation.ExcelMapper"})

public class ExportByMapProcessor extends AbstractProcessor {
	
	
	
	public ExportByMapProcessor() {
		super();
	}
 
	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latest();
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		int processed = 0;
		for (TypeElement annotation : annotations) {
			Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
			for (Element annotatedElement : annotatedElements) {
				for (Element enclosedElement : annotatedElement.getEnclosedElements()) {
					processingEnv.getElementUtils();
					System.out.println();
				}
			}
		}
		return processed > 1;
	}
}
