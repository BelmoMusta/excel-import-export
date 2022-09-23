package io.github.belmomusta.exporter.processor.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VelocityGenerator {
    private VelocityGenerator(){
        throw new IllegalStateException();
    }
    public static void generateCSVExporterJavaClassFile(VelocityWrapper wrapper, File destFile) throws IOException {
        common(wrapper, "CSVMapperTemplate.vm", destFile);
    
    }
    
    public static void generateExcelExporterJavaClassFile(VelocityWrapper wrapper, File destFile) throws IOException {
        common(wrapper, "ExcelMapperTemplate.vm", destFile);
    }
    
    private static void common(VelocityWrapper wrapper, String templateName, File destFile) throws IOException {
        if(wrapper == null) return;
        VelocityConfig config = new VelocityConfig(wrapper.isUseFQNs());
        config.setFullCurrentClassName(wrapper.getClassName());
        VelocityEngine ve = new VelocityEngine();
        final ClassLoader oldContextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(VelocityGenerator.class.getClassLoader());
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        ve.init();
        Template t = ve.getTemplate(templateName);
        VelocityContext context = new VelocityContext();
        context.put("config", config);
        context.put("wrapper", wrapper);
        destFile.getParentFile().mkdirs();
        FileWriter fileWriter = new FileWriter(destFile);
        t.merge(context, fileWriter);
        fileWriter.close();
        Thread.currentThread().setContextClassLoader(oldContextClassLoader);
    }
}
