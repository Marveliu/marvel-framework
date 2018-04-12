package com.marvel.framework.proxy;
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

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 代理链
 * @author Marveliu
 * @since 12/04/2018
 **/

public class ProxyChain {

    // 被代理类：类型，实例对象，目标方法，目标方法代理，方法参数
    private final Class<?> targetClass;
    private final Object targetObject;
    private final Method targetMethod;

    // cglib
    private final MethodProxy methodProxy;
    private final Object[] methodParams;

    // 代理列表
    private List<Proxy> proxyList = new ArrayList<Proxy>();
    // 代理索引
    private int proxyIndex = 0;


    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }


    public Object[] getMethodParams() {
        return methodParams;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object doProxyChain() throws Throwable {
        Object methodResult;
        if (proxyIndex < proxyList.size()) {
            methodResult = proxyList.get(proxyIndex++).doProxy(this);
        } else {
            // 执行目标对象本身的业务逻辑
            methodResult = methodProxy.invokeSuper(targetObject, methodParams);
        }
        return methodResult;
    }

}
