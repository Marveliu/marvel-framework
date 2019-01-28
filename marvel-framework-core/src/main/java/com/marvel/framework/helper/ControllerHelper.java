package com.marvel.framework.helper;

import com.marvel.framework.annotation.Action;
import com.marvel.framework.bean.Handler;
import com.marvel.framework.bean.Request;
import com.marvel.framework.util.ArrayUtil;
import com.marvel.framework.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 控制器助手类
 *
 * @author Marveliu
 * @since 11/04/2018
 **/
public final class ControllerHelper {

    /**
     * 存放请求和处理器的映射关系
     */
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static {
        // 获得controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtil.isNotEmpty(controllerClassSet)) {
            // 遍历controller
            for (Class<?> controllerClass : controllerClassSet) {
                // 遍历controller methods
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods)) {
                    for (Method method : methods) {
                        // 是否有action注解
                        if (method.isAnnotationPresent(Action.class)) {
                            // 加载action,获得URL映射规则
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();
                            // 验证URL映射规则，并添加到ACTION_MAP
                            if (mapping.matches("\\w+:/\\w*")) {
                                String[] array = mapping.split(":");
                                if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                                    // 获取请求方法和请求路径
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    // 获得request 记录请求方法和请求路径
                                    Request request = new Request(requestMethod, requestPath);
                                    // 获得处理器类 controller对象和对象方法
                                    Handler handler = new Handler(controllerClass, method);
                                    ACTION_MAP.put(request, handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取 Handler
     */
    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}

