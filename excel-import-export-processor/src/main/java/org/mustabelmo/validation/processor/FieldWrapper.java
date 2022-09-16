package org.mustabelmo.validation.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class FieldWrapper extends ElementWrapper {

    public FieldWrapper(Element field) {
        super(field);
    }

    public boolean isValid() {
        return !wrappedElement.getModifiers().contains(Modifier.STATIC);
    }

}
