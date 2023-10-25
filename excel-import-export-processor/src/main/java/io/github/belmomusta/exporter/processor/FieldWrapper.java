package io.github.belmomusta.exporter.processor;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class FieldWrapper extends ElementWrapper {
    
    public FieldWrapper(Element field) {
        super(field);
    }
    
    public boolean isValid() {
        return !wrappedElement.getModifiers().contains(Modifier.STATIC);
    }
    
    public boolean isFieldAnnotatedWithLombokGetter() {
        boolean withLombok = false;
        for (AnnotationMirror annotationMirror : wrappedElement.getAnnotationMirrors()) {
            if (annotationMirror.getAnnotationType().toString().equals("lombok.Getter")) {
                withLombok = true;
                break;
            }
        }
        return withLombok;
    }
    
}
