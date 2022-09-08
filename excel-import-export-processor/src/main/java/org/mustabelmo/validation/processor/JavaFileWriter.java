package org.mustabelmo.validation.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.File;

public class JavaFileWriter {
    public static void writeJavaClass(ClassWrapper classWrapper, ProcessingEnvironment processingEnv) {
        try {
            VelocityWrapper wrapper = classWrapper.getVelocityWrapper();
            final JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(classWrapper.getGeneratedClassName());
            VelocityGenerator.generateHtmlFile(wrapper, new File(builderFile.getName()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
