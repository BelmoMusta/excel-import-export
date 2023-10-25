package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.api.annotation.Export;
import io.github.belmomusta.exporter.api.common.ExportType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommonProcessor {
    protected final ProcessingEnvironment processingEnv;
    
    protected CommonProcessor(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }
    
    public boolean process(TypeElement annotation, RoundEnvironment roundEnv) {
        int processed = 0;
        try {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element aClass : annotatedElements) {
                if (!aClass.getModifiers().contains(Modifier.PUBLIC) || !(aClass instanceof TypeElement)) {
                    continue;
                }
                
                final List<ClassWrapper> classWrappers = getWrappers(aClass);
                process(classWrappers);
                processed++;
            }
        } catch (Exception e) {
            throw new JavaAnnotationProcessingException(e);
        }
        
        return processed > 0;
    }
    
    protected List<ClassWrapper> getWrappers(Element aClass) {
        List<ClassWrapper> wrappers = new ArrayList<>();
        Export export = aClass.getAnnotation(Export.class);
        if (export != null) {
            for (ExportType exportType : export.type()) {
                wrappers.add(ClassWrapper.of(aClass, exportType));
            }
        }
        return wrappers;
    }
    
    private void process(List<ClassWrapper> classWrappers) {
        for (ClassWrapper classWrapper : classWrappers) {
            if (classWrapper.getExportType() == ExportType.EXCEL) {
                JavaFileWriter.writeJavaClassForExcel(classWrapper, processingEnv);
            } else if (classWrapper.getExportType() == ExportType.CSV) {
                JavaFileWriter.writeJavaClassForCSV(classWrapper, processingEnv);
            }
        }
    }
    
    
}