package com.marvel.framework.helper;

import com.marvel.framework.annotation.Aspect;
import com.marvel.framework.annotation.Service;
import com.marvel.framework.proxy.AspectProxy;
import com.marvel.framework.proxy.Proxy;
import com.marvel.framework.proxy.ProxyManager;
import com.marvel.framework.proxy.TransactionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 方法拦截助手类
 *
 * @author Marveliu
 * @since 12/04/2018
 **/

public final class AopHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    static {
        try {
            // 获得代理类和目标类之间的关系
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            // 获得目标类和目标类上的代理链
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                // 生成代理
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                // 添加到Bean中
                // todo:Bean中间 Map<tagetClass,Object> Object可能不唯，Map必须
                // 代理目标一般是controller，不会被注入到其他里面
                BeanHelper.setBean(targetClass, proxy);
            }
        } catch (Exception e) {
            LOGGER.error("aop failure..");
        }
    }

    /**
     * 获得所有注解的目标类
     *
     * @param aspect
     * @return
     * @throws Exception
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        // 获取Aspect设置的注解信息
        Class<? extends Annotation> annotation = aspect.value();
        // 如果该注解信息不是Aspect类且不为空，添加所有有该注解的目标类
        if (annotation != null && !annotation.equals(Aspect.class)) {
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }


    /**
     * 获得代理类和目标类之间的关系
     *
     * @return
     * @throws Exception
     */
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {
        // 存储代理类和目标类结合关系之间的映射
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();
        // 添加切面代理
        addAspectProxy(proxyMap);
        // 添加事务代理
        addTransactionProxy(proxyMap);
        return proxyMap;
    }

    /**
     * 添加注解代理
     *
     * @param proxyMap
     * @throws Exception
     */
    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        // 获得代理类 必须继承AspectProxy和有Aspect注解
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> proxyClass : proxyClassSet) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                // 获得该代理类所有的目标类
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                // 代理类与目标类集合
                proxyMap.put(proxyClass, targetClassSet);
            }
        }
    }

    /**
     * Service注解类绑定事务代理类
     *
     * @param proxyMap
     * @throws Exception
     */
    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        // 获得所有的事务类
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(Service.class);
        proxyMap.put(TransactionProxy.class, proxyClassSet);
    }


    /**
     * 获得目标类和目标类上的代理链
     *
     * @param proxyMap 代理类代理的所有目标类
     * @return
     * @throws Exception
     */
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        // 遍历代理类和其所有代理目标类的集合
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            // 代理类
            Class<?> proxyClass = proxyEntry.getKey();
            // 代理目标类集合
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            // 遍历代理目标类
            for (Class<?> targetClass : targetClassSet) {
                // 获得当前代理目标类的代理实例
                // todo:单例模式？
                Proxy proxy = (Proxy) proxyClass.newInstance();
                // 查询目标类是否有代理链
                if (targetMap.containsKey(targetClass)) {
                    // 添加代理链
                    // todo:可以设计代理的优先级顺序
                    targetMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass, proxyList);
                }
            }
        }
        return targetMap;
    }
}
