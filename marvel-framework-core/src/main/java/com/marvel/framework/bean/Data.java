package com.marvel.framework.bean;

/**
 * 返回数据对象
 *
 * @author Marveliu
 * @since 11/04/2018
 **/

public class Data {
    /**
     * 模型数据
     */
    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
