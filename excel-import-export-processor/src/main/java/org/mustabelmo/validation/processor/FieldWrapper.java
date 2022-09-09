package org.mustabelmo.validation.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class FieldWrapper {
    final Element wrappedField;

    private List<Annotation> annotations = new ArrayList<>();

    public FieldWrapper(Element field) {
        this.wrappedField = field;
    }

    public String getName() {
        return wrappedField.getSimpleName().toString();
    }

    public boolean isValid() {

        return !wrappedField.getModifiers().contains(Modifier.STATIC);
    }

    public TypeKind getKind() {
        ExecutableElement executableMethod = (ExecutableElement) wrappedField;
        return executableMethod.getReturnType().getKind();
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        for (Annotation anno : annotations) {
            if (anno.annotationType() == annotationType) {
                return (A) anno;
            }
        }
        return wrappedField.getAnnotation(annotationType);
    }

    public boolean add(Annotation annotation) {
        return annotations.add(annotation);
    }

}
