package org.mustabelmo.validation.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class MethodWrapper extends ElementWrapper {

    public MethodWrapper(Element method) {
        super(method);
    }

    @Override
    public boolean isValid() {
        final ExecutableElement executableMethod = (ExecutableElement) wrappedElement;
        TypeMirror returnType = ((ExecutableElement) wrappedElement).getReturnType();


        return returnType.getKind() != TypeKind.VOID
                && !executableMethod.getModifiers().contains(Modifier.ABSTRACT)
                && !executableMethod.getModifiers().contains(Modifier.PRIVATE)
                && executableMethod.getParameters().isEmpty();
    }

    public String getPossibleFieldName() {
        String ret;
        String correspondantFieldName = wrappedElement.getSimpleName().toString();
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
}
