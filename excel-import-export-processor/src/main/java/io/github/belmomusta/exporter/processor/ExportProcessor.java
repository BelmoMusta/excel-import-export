package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.api.annotation.Export;
import io.github.belmomusta.exporter.api.annotation.ToColumn;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes({
        "io.github.belmomusta.exporter.api.annotation.Export"
})

public class ExportProcessor extends AbstractProcessor {
    public static ProcessingEnvironment processingEnvironment;
    CommonProcessor processor;
    
    public ExportProcessor() {
        super();
        AnnotationsRegistrer.register(ToColumn.class);
    }
    
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        processor = new CommonProcessor(processingEnv);
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
            if (annotation.getQualifiedName().toString().equals(Export.class.getName())) {
                processor.process(annotation, roundEnv);
                processed++;
            }
        }
        return processed > 1;
    }
}
