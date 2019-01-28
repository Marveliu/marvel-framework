package com.marvel.plugin.security;


import org.apache.shiro.web.env.EnvironmentLoaderListener;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

/**
 * 集成的ServletContainer
 * 基于SPI
 *
 * @author Marveliu
 * @since 18/04/2018
 **/
public class MarvelSecurityPlugin implements ServletContainerInitializer {

    // todo servletContainerInitializer
    public void onStartup(Set<Class<?>> handlesTypes, ServletContext servletContext) throws ServletException {
        // 设置初始化参数,改变shiro.ini->marvel-security.ini
        servletContext.setInitParameter("shiroConfigLocations", "classpath:marvel-security.ini");
        // 注册 Listener
        servletContext.addListener(EnvironmentLoaderListener.class);
        // 注册 Filter
        FilterRegistration.Dynamic MarvelSecurityFilter = servletContext.addFilter("MarvelSecurityFilter", MarvelSecurityFilter.class);
        MarvelSecurityFilter.addMappingForUrlPatterns(null, false, "/*");
    }
}

