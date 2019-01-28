package com.marvel.framework.bean;

import java.lang.reflect.Method;

/**
 * 封装action
 * 关联action和对应的controller
 *
 * @author Marveliu
 * @since 11/04/2018
 **/

public class Handler {

    /**
     * Controller 类
     */
    private Class<?> controllerClass;

    /**
     * Action 对应的方法
     */
    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
