package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.api.annotation.CSV;
import io.github.belmomusta.exporter.api.annotation.Excel;
import io.github.belmomusta.exporter.api.annotation.ToColumn;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;
@SupportedAnnotationTypes({
		"io.github.belmomusta.exporter.api.annotation.Excel",
		"io.github.belmomusta.exporter.api.annotation.CSV",
})

public class ExportProcessor extends AbstractProcessor {
	public static ProcessingEnvironment processingEnvironment;
	CommonProcessor excel;
	CommonProcessor csv;
	
	public ExportProcessor() {
		super();
		AnnotationsRegistrer.register(ToColumn.class);
	}
	
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		excel = new ExcelProcessor(processingEnv);
		csv = new CSVProcessor(processingEnv);
		processingEnvironment = processingEnv;
	}
	
	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latest();
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		int processed = 0;
		for (TypeElement annotation : annotations) {
			if (annotation.getQualifiedName().toString().equals(Excel.class.getName())) {
				excel.process(annotation, roundEnv);
				processed++;
			} else if (annotation.getQualifiedName().toString().equals(CSV.class.getName())) {
				csv.process(annotation, roundEnv);
				processed++;
				
			}
		}
		return processed > 1;
	}
}
