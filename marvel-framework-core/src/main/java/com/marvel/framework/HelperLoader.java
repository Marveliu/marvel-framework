package com.marvel.framework;

import com.marvel.framework.helper.*;
import com.marvel.framework.util.ClassUtil;

/**
 * 加载相应的 Helper 类
 * 确定IOC容器类初始化的顺序
 *
 * @author Marveliu
 * @since 11/04/2018
 **/
public final class HelperLoader {

    public static void init() {
        Class<?>[] classList = {
                // 获得所有的class
                ClassHelper.class,
                // 获得所有的Bean
                BeanHelper.class,
                // 替换所有需要AOP的Bean
                AopHelper.class,
                // Aop获得代理对象，Ioc才能依赖注入
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName());
        }
    }
}