package io.github.belmomusta.exporter.processor;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;

@SupportedAnnotationTypes("io.github.belmomusta.exporter.api.annotation.Excel")
public class ExcelProcessor extends CommonProcessor {
	
	@Override
	protected ClassWrapper getWrapper(Element aClass) {
		return ClassWrapper.of(aClass);
	}
	
	@Override
    protected void process(ClassWrapper classWrapper) {
        JavaFileWriter.writeJavaClassForExcel(classWrapper, processingEnv);
    }
}