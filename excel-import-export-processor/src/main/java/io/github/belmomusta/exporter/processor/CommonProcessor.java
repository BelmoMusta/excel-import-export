package io.github.belmomusta.exporter.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public abstract class CommonProcessor  {
	protected final	ProcessingEnvironment processingEnv;
	
	protected CommonProcessor(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
	
	public boolean process(TypeElement annotation, RoundEnvironment roundEnv) {
		int processed = 0;
		try {
			Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
			for (Element aClass : annotatedElements) {
				final ClassWrapper classWrapper = getWrapper(aClass);
				process(classWrapper);
				processed++;
			}
		} catch (Exception e) {
			throw new JavaAnnotationProcessingException(e);
		}
		
		return processed > 0;
	}
	
	protected abstract ClassWrapper getWrapper(Element aClass);
	
	protected abstract void process(ClassWrapper classWrapper);
	
	
}