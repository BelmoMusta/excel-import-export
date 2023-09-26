package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.processor.velocity.VelocityGenerator;
import io.github.belmomusta.exporter.processor.velocity.VelocityWrapper;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.Writer;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class JavaFileWriter {
	
	private JavaFileWriter() {
		throw new IllegalStateException();
	}
	
	public static void writeJavaClassForExcel(ClassWrapper classWrapper, ProcessingEnvironment processingEnv) {
		common(classWrapper, processingEnv, JavaFileWriter::excel);
	}
	
	public static void writeJavaClassForCSV(ClassWrapper classWrapper, ProcessingEnvironment processingEnv) {
		common(classWrapper, processingEnv, JavaFileWriter::csv);
	}
	
	private static String excel(VelocityWrapper wrapper, JavaFileObject builderFile) {
		try {
			return VelocityGenerator.generateExcelExporterJavaClassFile(wrapper);
		} catch (Exception e) {
			throw new JavaFileWriterException(e);
		}
	}
	
	private static String csv(VelocityWrapper wrapper, JavaFileObject builderFile) {
		try {
			return VelocityGenerator.generateCSVExporterJavaClassFile(wrapper);
		} catch (Exception e) {
			throw new JavaFileWriterException(e);
		}
	}
	
	private static void common(ClassWrapper classWrapper, ProcessingEnvironment processingEnv,
							   BiFunction<VelocityWrapper, JavaFileObject, String> consumer) {
		try {
			VelocityWrapper wrapper = WrapperUtils.getVelocityWrapper(classWrapper);
			final JavaFileObject builderFile = processingEnv.getFiler()
					.createSourceFile(classWrapper.getFQNOfGeneratedClass());
            Writer out = builderFile.openWriter();
			String classFile = consumer.apply(wrapper, builderFile);
            out.write(classFile);
            out.close();
   
			
		} catch (Exception e) {
			throw new JavaFileWriterException(e);
		}
	}
}
