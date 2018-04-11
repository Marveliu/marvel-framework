package com.marvel.framework.helper;
/*
 * Copyright [2018] [Marveliu]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.marvel.framework.annotation.Inject;
import com.marvel.framework.util.ArrayUtil;
import com.marvel.framework.util.CollectionUtil;
import com.marvel.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Marveliu
 * @since 11/04/2018
 **/

public final class IocHelper {

    static {

        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();

        // 判断bean是否为空
        if (CollectionUtil.isNotEmpty(beanMap)) {

            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {

                // 获得bean 类，实例对象，属性
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();

                // 获取bean上所有成员变量
                Field[] beanFields = beanClass.getDeclaredFields();

                // 遍历属性，是否被inject注解
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    for (Field beanField : beanFields) {

                        if (beanField.isAnnotationPresent(Inject.class)) {

                            // 获得属性类型
                            Class<?> beanFieldClass = beanField.getType();
                            // 在beanMap中查询Bean Filed的实例
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null) {
                                // 反射初始化 BeanFiled的值
                                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                            }
                        }

                    }
                }
            }
        }
    }
}
