package io.github.belmomusta.exporter.processor;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

public class LombokMethodWrapper extends MethodWrapper {
    
    public LombokMethodWrapper(Element method) {
        super(method);
    }
    
    @Override
    public boolean isValid() {
        boolean lombokAccessDenied = false;
        for (AnnotationMirror annotationMirror : wrappedElement.getAnnotationMirrors()) {
            if (annotationMirror.getAnnotationType().toString().equals("lombok.Getter")) {
                lombokAccessDenied = annotationMirror.getElementValues().entrySet().stream()
                        .filter(o -> o.getKey().toString().equals("value()"))
                        .anyMatch(o -> !o.getValue().toString().equals("lombok.AccessLevel.PUBLIC"));
                if (lombokAccessDenied) {
                    break;
                }
            }
        }
        return !lombokAccessDenied && !isStaticMethod();
    }
    
    @Override
    public String getName() {
        return "get" + WrapperUtils.capitalize(super.getName());
    }
}
