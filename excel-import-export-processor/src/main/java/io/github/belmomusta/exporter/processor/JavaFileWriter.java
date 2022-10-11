package io.github.belmomusta.exporter.processor;

import io.github.belmomusta.exporter.processor.velocity.VelocityGenerator;
import io.github.belmomusta.exporter.processor.velocity.VelocityWrapper;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.File;
import java.util.function.BiConsumer;

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
	
	private static void excel(VelocityWrapper wrapper, JavaFileObject builderFile) {
		try {
			VelocityGenerator.generateExcelExporterJavaClassFile(wrapper, new File(builderFile.getName()));
		} catch (Exception e) {
			throw new JavaFileWriterException(e);
		}
	}
	
	private static void csv(VelocityWrapper wrapper, JavaFileObject builderFile) {
		try {
			VelocityGenerator.generateCSVExporterJavaClassFile(wrapper, new File(builderFile.getName()));
		} catch (Exception e) {
			throw new JavaFileWriterException(e);
		}
	}
	
	private static void common(ClassWrapper classWrapper, ProcessingEnvironment processingEnv,
							   BiConsumer<VelocityWrapper, JavaFileObject> consumer) {
		try {
			VelocityWrapper wrapper = WrapperUtils.getVelocityWrapper(classWrapper);
			final JavaFileObject builderFile = processingEnv.getFiler()
					.createSourceFile(classWrapper.getFQNOfGeneratedClass());
			consumer.accept(wrapper, builderFile);
			
		} catch (Exception e) {
			throw new JavaFileWriterException(e);
		}
	}
}
