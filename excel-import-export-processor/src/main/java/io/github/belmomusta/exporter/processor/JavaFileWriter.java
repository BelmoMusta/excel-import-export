package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.processor.velocity.VelocityGenerator;
import io.github.belmomusta.exporter.processor.velocity.VelocityWrapper;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.File;

public class JavaFileWriter {
    private JavaFileWriter(){
        throw new IllegalStateException();
    }
    public static void writeJavaClassForExcel(ClassWrapper classWrapper, ProcessingEnvironment processingEnv) {
        try {
            VelocityWrapper wrapper = classWrapper.getVelocityWrapper();
            final JavaFileObject builderFile = processingEnv.getFiler()
                    .createSourceFile(classWrapper.getFQNOfGeneratedClass());
            VelocityGenerator.generateExcelExporterJavaClassFile(wrapper, new File(builderFile.getName()));
        } catch (Exception e) {
            throw new JavaFileWriterException(e);
        }
    }

    public static void writeJavaClassForCSV(ClassWrapper classWrapper, ProcessingEnvironment processingEnv) {
        try {
            VelocityWrapper wrapper = classWrapper.getVelocityWrapper();
            final JavaFileObject cSVExportbuilderFile = processingEnv.getFiler()
                    .createSourceFile(classWrapper.getFQNOfGeneratedClass());
            VelocityGenerator.generateCSVExporterJavaClassFile(wrapper, new File(cSVExportbuilderFile.getName()));
            
        } catch (Exception e) {
            throw new JavaFileWriterException(e);
        }
    }
}
