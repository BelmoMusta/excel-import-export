package io.github.belmomusta.exporter.processor.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.helpers.NOPLogger;

import java.io.IOException;
import java.io.StringWriter;

public class VelocityGenerator {
    private VelocityGenerator() {
        throw new IllegalStateException();
    }
    
    public static String generateCSVExporterJavaClassFile(VelocityWrapper wrapper) throws IOException {
        return common(wrapper, "CSVMapperTemplate.vm");
    }
    
    public static String generateExcelExporterJavaClassFile(VelocityWrapper wrapper) throws IOException {
        return common(wrapper, "ExcelMapperTemplate.vm");
    }
    
    private static String common(VelocityWrapper wrapper, String templateName) throws IOException {
        if (wrapper == null) return null;
        VelocityConfig config = new VelocityConfig(wrapper.isUseFQNs());
        config.setFullCurrentClassName(wrapper.getClassName());
        VelocityEngine ve = new VelocityEngine();
        final ClassLoader oldContextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(VelocityGenerator.class.getClassLoader());
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("runtime.log.instance", NOPLogger.NOP_LOGGER);
        ve.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        ve.init();
        Template t = ve.getTemplate(templateName);
        VelocityContext context = new VelocityContext();
        context.put("config", config);
        context.put("wrapper", wrapper);
        StringWriter stringWriter = new StringWriter();
        t.merge(context, stringWriter);
        stringWriter.close();
        Thread.currentThread().setContextClassLoader(oldContextClassLoader);
        return stringWriter.toString();
    }
}
