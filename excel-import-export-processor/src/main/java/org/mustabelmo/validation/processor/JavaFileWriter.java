package org.mustabelmo.validation.processor;

import org.mustabelmo.validation.processor.velocity.VelocityGenerator;
import org.mustabelmo.validation.processor.velocity.VelocityWrapper;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.File;

public class JavaFileWriter {
    private JavaFileWriter(){
        throw new IllegalStateException();
    }
    public static void writeJavaClass(ClassWrapper classWrapper, ProcessingEnvironment processingEnv) {
        try {
            VelocityWrapper wrapper = classWrapper.getVelocityWrapper();
            final JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(classWrapper.getFQNOfGeneratedClass());
            VelocityGenerator.generateJavaClassFile(wrapper, new File(builderFile.getName()));

        } catch (Exception e) {
            throw new JavaFileWriterException(e);
        }
    }
}
