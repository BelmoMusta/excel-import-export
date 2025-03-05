package io.github.mustabelmo.exporter;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import io.github.mustabelmo.exporter.processor.velocity.VelocityConfig;
import io.github.mustabelmo.exporter.processor.velocity.VelocityWrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Mustashe {
    
    public static void main(String[] args) throws IOException {
        VelocityWrapper wrapper = new VelocityWrapper();
        
        wrapper.setSimplifiedClassName("test");
        wrapper.setClassName("test");
        wrapper.setAPackage("com.test");
        wrapper.setCorrespondanceFieldMethod(Arrays.asList());
        
        VelocityConfig config = new VelocityConfig(wrapper.isUseFQNs());
        config.setFullCurrentClassName(wrapper.getClassName());
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("ClassNameMethod.mustache");
        
        Map context = new LinkedHashMap();
        context.put("config", config);
        context.put("wrapper", wrapper);
        context.put("exporterName", "CSVExporter");
        context.put("classToExtend", config.getAbstractCSVExporterClassName());
        mustache.execute(new PrintWriter(System.out), context).flush();
    }
}

class Example {
    public String string() {
        return "test test";
    }
    
    List<Item> items() {
        return Arrays.asList(
                new Item("Item 1", "$19.99", Arrays.asList(new Feature("New!"), new Feature("Awesome!"))),
                new Item("Item 2", "$29.99", Arrays.asList(new Feature("Old."), new Feature("Ugly.")))
        );
    }
    
    static class Item {
        Item(String name, String price, List<Feature> features) {
            this.name = name;
            this.price = price;
            this.features = features;
        }
        
        String name, price;
        List<Feature> features;
    }
    
    static class Feature {
        Feature(String description) {
            this.description = description;
        }
        
        String description;
    }
}
