package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.api.annotation.ToColumn;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public abstract class CommonProcessor extends AbstractProcessor {
	
	protected CommonProcessor() {
		AnnotationsRegistrer.register(ToColumn.class);
	}
	
	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latest();
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		int processed = 0;
		try {
			for (TypeElement annotation : annotations) {
				Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
				for (Element aClass : annotatedElements) {
					final ClassWrapper classWrapper = getWrapper(aClass);
					process(classWrapper);
					processed++;
				}
			}
		} catch (Exception e) {
			throw new JavaAnnotationProcessingException(e);
		}
		
		return processed > 0;
	}
	
	protected abstract ClassWrapper getWrapper(Element aClass);
	
	protected abstract void process(ClassWrapper classWrapper);
	
	
}