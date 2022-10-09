package io.github.belmomusta.exporter.processor;

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
                //&& !executableMethod.getModifiers().contains(Modifier.STATIC)
                && !executableMethod.getModifiers().contains(Modifier.PRIVATE)
                && executableMethod.getParameters().isEmpty();
    }

    public String getPossibleFieldName() {
        String ret;
        String relatedFieldName = wrappedElement.getSimpleName().toString();
        if (relatedFieldName.startsWith("is") && relatedFieldName.length() > 2) {
            ret = relatedFieldName.substring(2);
        } else if (relatedFieldName.startsWith("get") && relatedFieldName.length() > 3) {
            ret = relatedFieldName.substring(3);
        } else {
            ret = null;
        }
        if (ret != null) {
            ret = uncapitalize(ret);
        }
        return ret;
    }
	
	public boolean isStaticMethod() {
		return wrappedElement.getModifiers().contains(Modifier.STATIC);
	}
}
