package org.mustabelmo.validation.processor;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

public class AnnotationsRegistrer {
    public static final Set<Class<? extends Annotation>> ANNOTATIONS = new LinkedHashSet<>();

    public static void register(Class<? extends Annotation> annotation) {
        ANNOTATIONS.add(annotation);
    }

    public static void remove(Class<? extends Annotation> annotation) {
        ANNOTATIONS.remove(annotation);
    }
}