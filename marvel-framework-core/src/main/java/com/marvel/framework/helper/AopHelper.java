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

import com.marvel.framework.annotation.Aspect;
import com.marvel.framework.proxy.AspectProxy;
import com.marvel.framework.proxy.Proxy;
import com.marvel.framework.proxy.ProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 方法拦截助手类
 * @author Marveliu
 * @since 12/04/2018
 **/

public final class AopHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);


    static{
        try {
            // 获得代理类和目标类之间的关系
            Map<Class<?>,Set<Class<?>>> proxyMap = createProxyMap();
            // 获得目标类和目标类上的代理链
            Map<Class<?>,List<Proxy>> targetMap = createTargetMap(proxyMap);
            for(Map.Entry<Class<?>,List<Proxy>> targetEntry:targetMap.entrySet()){
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                // 生成代理
                Object proxy = ProxyManager.createProxy(targetClass,proxyList);
                // 添加到Bean中
                // todo:Bean中间 Map<tagetClass,Object> Object可能不唯，Map必须
                // 代理目标一般是controller，不会被注入到其他里面
                BeanHelper.setBean(targetClass,proxy);
            }
        }catch (Exception e){
            LOGGER.error("aop failure..");
        }
    }

    // 获得目标类
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception{
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        // 获取Aspect设置的注解信息
        Class<? extends Annotation> annotation = aspect.value();
        // 如果该注解信息不是Aspect类且不为空，添加所有有该注解的目标类
        if(annotation != null  && !annotation.equals(Aspect.class)){
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }


    /**
     * 获得代理类和目标类之间的关系
     * @return
     * @throws Exception
     */
    private static Map<Class<?>,Set<Class<?>>> createProxyMap() throws Exception{
        // 存储代理类和目标类结合关系之间的映射
        Map<Class<?>,Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();

        // 获得代理类 必须继承AspectProxy和有Aspect注解
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for(Class<?> proxyClass:proxyClassSet){
            if(proxyClass.isAnnotationPresent(Aspect.class)){
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                // 获得该代理类所有的目标类
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass,targetClassSet);
            }
        }
        return proxyMap;
    }


    /**
     * 获得目标类和目标类上的代理链
     * @param proxyMap  代理类代理的所有目标类
     * @return
     * @throws Exception
     */
    private static Map<Class<?>,List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception{

        Map<Class<?>,List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        // 遍历代理类和其所有代理目标类的集合
        for(Map.Entry<Class<?>,Set<Class<?>>> proxyEntry : proxyMap.entrySet()){
            // 代理类
            Class<?> proxyClass = proxyEntry.getKey();
            // 代理目标类集合
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            // 遍历代理目标类
            for(Class<?> targetClass : targetClassSet){
                // 获得当前代理目标类的代理实例
                // todo:单例模式？
                Proxy proxy = (Proxy) proxyClass.newInstance();
                // 查询目标类是否有代理链
                if(targetMap.containsKey(targetClass)){
                    // 添加代理链
                    // todo:可以设计代理的优先级顺序
                    targetMap.get(targetClass).add(proxy);
                }else {
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass,proxyList);
                }
            }
        }

        return  targetMap;
    }
}
