package io.github.belmomusta.exporter.processor;

public class JavaAnnotationProcessingException extends RuntimeException {
    public JavaAnnotationProcessingException(Exception e) {
        super(e);
    }
}
