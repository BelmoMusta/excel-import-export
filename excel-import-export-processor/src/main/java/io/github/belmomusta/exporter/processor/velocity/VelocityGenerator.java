package io.github.mustabelmo.exporter.processor.velocity;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.helpers.NOPLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class VelocityGenerator {
    private VelocityGenerator() {
        throw new IllegalStateException();
    }
    
    public static String generateCSVExporterJavaClassFile(VelocityWrapper wrapper) throws IOException {
        return common(wrapper, "CSVMapperTemplate.mustache");
    }
    
    public static String generateExcelExporterJavaClassFile(VelocityWrapper wrapper) throws IOException {
        return common(wrapper, "ExcelMapperTemplate.mustache");
    }
    
    private static String common(VelocityWrapper wrapper, String templateName) throws IOException {
        if (wrapper == null) return null;
        
        VelocityConfig config = new VelocityConfig(wrapper.isUseFQNs());
        config.setFullCurrentClassName(wrapper.getClassName());
        
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(templateName);
        Map context = new LinkedHashMap();
        context.put("config", config);
        context.put("wrapper", wrapper);
        
        StringWriter stringWriter = new StringWriter();
        mustache.execute(stringWriter, context).flush();
        return stringWriter.toString();
    }
    
    public static void main(String[] args) throws Exception {
        VelocityWrapper wrapper = new VelocityWrapper();
        wrapper.setAPackage("toto");
        wrapper.setSimplifiedClassName("test");
        wrapper.setClassName("test");
        wrapper.setAPackage("com.test");
        wrapper.setSimplifiedClassName("Car");
        wrapper.setCorrespondanceFieldMethod(Arrays.asList());
        FieldMethodPair fieldMethodPair = new FieldMethodPair("toto");
        fieldMethodPair.setFormatter("FOOBarFotmatter");
        wrapper.setCorrespondanceFieldMethod(Arrays.asList(fieldMethodPair));
        wrapper.setImports(Arrays.asList("a", "b"));
        String common = common(wrapper, "CSVMapperTemplate.mustache");
        System.out.println(common);
        
    }
}
