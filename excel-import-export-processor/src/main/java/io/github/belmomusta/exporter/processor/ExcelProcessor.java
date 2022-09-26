package io.github.belmomusta.exporter.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

public class ExcelProcessor extends CommonProcessor {
	
	public ExcelProcessor(ProcessingEnvironment processingEnv) {
		super(processingEnv);
	}
	
	@Override
	protected ClassWrapper getWrapper(Element aClass) {
		return ClassWrapper.of(aClass);
	}
	
	@Override
    protected void process(ClassWrapper classWrapper) {
        JavaFileWriter.writeJavaClassForExcel(classWrapper, processingEnv);
    }
}