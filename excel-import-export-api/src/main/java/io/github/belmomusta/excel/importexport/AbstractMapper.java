package io.github.belmomusta.excel.importexport;

import io.github.belmomusta.excel.importexport.annotation.ExcelColumn;

import java.lang.reflect.Member;

public abstract class AbstractMapper {

    protected void mapAnnotatedMembers(Class target) {
        map(target.getDeclaredMethods());
        map(target.getDeclaredFields());
    }

    protected void map(Member[] members) {
        Wrapper[] wrappers = Wrapper.wrap(members);
        for (Wrapper wrapper : wrappers) {
            final ExcelColumn excelColumn = wrapper.getAnnotation(ExcelColumn.class);
            if (excelColumn != null) {
                map(wrapper.getName()).toCell(excelColumn.value());
            }
        }
    }

    abstract protected <T> ColumnsMapper<T> map(String columnName);
}