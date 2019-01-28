package com.marvel.framework.proxy;

/**
 * 代理接口
 *
 * @author Marveliu
 * @since 12/04/2018
 **/

public interface Proxy {

    /**
     * 执行链式代理
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}