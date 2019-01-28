package com.marvel.framework.util;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Set;

public class ClassUtilTest {


    @Before
    public void init() throws Exception {
    }

    @Test
    public void getClassLoader() {
        ClassLoader loader = ClassUtil.getClassLoader();
        Field[] fields = loader.getClass().getDeclaredFields();
        int count = 0;
        for (Field item : fields) {
            System.out.println(count + ":" + item.getName());
            count++;
        }
        System.out.println("classloader:" + loader.getClass().getName());
        System.out.println("local path:" + loader.getResource("."));


    }

    @Test
    public void loadClass() {
        Set<Class<?>> Csets = ClassUtil.getClassSet("com.marvel");
        for (Class<?> item : Csets) {
            System.out.println("classname:" + item.getName());
        }
    }

}