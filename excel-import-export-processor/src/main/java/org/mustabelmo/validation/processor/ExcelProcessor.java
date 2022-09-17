package org.mustabelmo.validation.processor;


import io.github.belmomusta.excel.api.annotation.ExcelColumn;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;


@SupportedAnnotationTypes("io.github.belmomusta.excel.api.annotation.ExcelRow")
public class ExcelProcessor extends AbstractProcessor {
    
    public ExcelProcessor() {
        AnnotationsRegistrer.register(ExcelColumn.class);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        try {
            for (TypeElement annotation : annotations) {

                Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
                for (Element aClass : annotatedElements) {
                    final ClassWrapper classWrapper = ClassWrapper.of(aClass);
                    JavaFileWriter.writeJavaClass(classWrapper, processingEnv);
                }
            }
        } catch (Exception e) {
            throw new JavaAnnotationProcessingException(e);
        }

        return true;
    }


}