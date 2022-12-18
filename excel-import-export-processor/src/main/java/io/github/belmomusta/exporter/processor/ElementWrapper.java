package io.github.belmomusta.exporter.processor;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public abstract class ElementWrapper {
    protected final Element wrappedElement;

    private final List<Annotation> annotations = new ArrayList<>();
    
    String formatter;

    protected ElementWrapper(Element method) {
        this.wrappedElement = method;
    }

    public String getName() {
        return wrappedElement.getSimpleName().toString();
    }

    public abstract boolean isValid();
	
	public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        for (Annotation anno : annotations) {
            if (anno.annotationType() == annotationType) {
                return (A) anno;
            }
        }
        return wrappedElement.getAnnotation(annotationType);
    }

    public boolean add(Annotation annotation) {
        return annotations.add(annotation);
    }
    
    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }
    
    public String getFormatter() {
        return formatter;
    }
}
