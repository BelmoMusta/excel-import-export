package org.mustabelmo.validation.processor.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VelocityGenerator {
    private VelocityGenerator(){
        throw new IllegalStateException();
    }
    public static void generateJavaClassFile(VelocityWrapper wrapper, File destFile) throws IOException {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty("runtime.log.logsystem.logger", NullLogChute.class.getName());
        final ClassLoader oldContextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(VelocityGenerator.class.getClassLoader());
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        ve.init();
        Template t = ve.getTemplate(getTemplateName(wrapper.isUseFQNs()));
        VelocityContext context = new VelocityContext();
        context.put("wrapper", wrapper);
        destFile.getParentFile().mkdirs();
        FileWriter fileWriter = new FileWriter(destFile);
        t.merge(context, fileWriter);
        fileWriter.close();
        Thread.currentThread().setContextClassLoader(oldContextClassLoader);

    }
    
    private static String getTemplateName(boolean isUseFQNs) {
        if(isUseFQNs) {
            return "ExcelMapperTemplateWithFQNs.vm";
        }
        return "ExcelMapperTemplate.vm";
        
    }
    
}
