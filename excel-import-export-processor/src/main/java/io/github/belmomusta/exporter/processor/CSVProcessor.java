package io.github.belmomusta.exporter.processor;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;


@SupportedAnnotationTypes("io.github.belmomusta.exporter.api.annotation.CSV")
public class CSVProcessor extends CommonProcessor {
	
	@Override
	protected ClassWrapper getWrapper(Element aClass) {
		return ClassWrapper.ofCSV(aClass);
	}
	
	@Override
	protected void process(ClassWrapper classWrapper) {
		JavaFileWriter.writeJavaClassForCSV(classWrapper, processingEnv);
	}
}