package io.github.belmomusta.excel.importexport;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public class Wrapper {
    Member member;

    public Wrapper(Member member) {
        this.member = member;
    }

    public <U extends Annotation> U getAnnotation(Class<U> annotationClass) {
        if (member instanceof Field) {
            return ((Field) member).getAnnotation(annotationClass);
        }

        if (member instanceof Method) {
            return ((Method) member).getAnnotation(annotationClass);
        }
        return null;
    }

    static Wrapper[] wrap(Member[] members) {

        Wrapper[] wrappers = new Wrapper[members.length];
        for (int i = 0; i < members.length; i++) {
            Wrapper wrapper = new Wrapper(members[i]);
            wrappers[i] = wrapper;
        }
        return wrappers;
    }

    public String getName() {
        if (member instanceof Method) {
            return '#' + member.getName();
        }
        return member.getName();
    }
}
