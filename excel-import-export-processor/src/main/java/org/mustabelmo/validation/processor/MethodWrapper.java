package org.mustabelmo.validation.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class MethodWrapper {
    final Element wrappedMethod;

    private List<Annotation> annotations = new ArrayList<>();

    public MethodWrapper(Element method) {
        this.wrappedMethod = method;
    }

    public String getName() {
        return wrappedMethod.getSimpleName().toString();
    }

    public boolean isMethod() {
        return wrappedMethod.getKind() == ElementKind.METHOD;
    }

    public boolean isValid() {
        final ExecutableElement executableMethod = (ExecutableElement) wrappedMethod;
        final TypeKind kind = getKind();
        return  kind != TypeKind.VOID
                && !executableMethod.getModifiers().contains(Modifier.ABSTRACT)
                && !executableMethod.getModifiers().contains(Modifier.PRIVATE);
    }

    public TypeKind getKind() {
        ExecutableElement executableMethod = (ExecutableElement) wrappedMethod;
        return executableMethod.getReturnType().getKind();
    }

    public String getPossibleFieldName() {
        String ret;
        String correspondantFieldName = wrappedMethod.getSimpleName().toString();
        if (correspondantFieldName.startsWith("is") && correspondantFieldName.length() > 2) {
            ret = correspondantFieldName.substring(2);
        } else if (correspondantFieldName.startsWith("get") && correspondantFieldName.length() > 3) {
            ret = correspondantFieldName.substring(3);
        } else {
            ret = null;
        }
        if (ret != null) {
            ret = uncapitalize(ret);
        }
        return ret;
    }

    private String uncapitalize(String correspondantFieldName) {
        char c = correspondantFieldName.charAt(0);
        c = Character.toLowerCase(c);
        return c + correspondantFieldName.substring(1);

    }

    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        for (Annotation anno : annotations) {
            if (anno.annotationType() == annotationType) {
                return (A) anno;
            }
        }
        return wrappedMethod.getAnnotation(annotationType);
    }

    public boolean add(Annotation annotation) {
        return annotations.add(annotation);
    }

}
