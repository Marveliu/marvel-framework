package com.marvel.framework.bean;

/**
 * 封装表单参数
 *
 * @author Marveliu
 * @since 13/04/2018
 **/

public class FormParam {

    private String fieldName;
    private Object fieldValue;

    public FormParam(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
