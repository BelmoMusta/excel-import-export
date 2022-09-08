package org.mustabelmo.validation.processor;

public interface ParamNameStrategy {
    default String getParam(String param) {
        return "p" + capitalize(param);
    }

    static String capitalize(String param) {
        if (param == null) {
            return null;
        }
        if (param.isEmpty()) {
            return param;
        }
        return Character.toUpperCase(param.charAt(0)) + param.substring(1);
    }
}
