package com.marvel.framework;
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

import com.marvel.framework.bean.Data;
import com.marvel.framework.bean.Handler;
import com.marvel.framework.bean.Param;
import com.marvel.framework.bean.View;
import com.marvel.framework.helper.BeanHelper;
import com.marvel.framework.helper.ConfigHelper;
import com.marvel.framework.helper.ControllerHelper;
import com.marvel.framework.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求转发器
 * @author Marveliu
 * @since 11/04/2018
 **/

@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {


    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        // 初始化helperloader
        HelperLoader.init();;
        // 获取ServletContext对象，注册Servlet
        ServletContext servletContext =  servletConfig.getServletContext();
        // 注册处理jsp servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
        // 注册处理静态资源的servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        // 获得请求方法和请求路径
        String requestMethod = request.getMethod().toLowerCase();
        String requsetPath = request.getPathInfo();
        // 获得Action处理器
        Handler handler = ControllerHelper.getHandler(requestMethod,requsetPath);

        // 获取Controller类及其Bean实例
        if(handler != null){
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);

            // 创建请求参数对象
            Map<String,Object> paramMap = new HashMap<String, Object>();

            // request 头部
            Enumeration<String> paramNames = request.getParameterNames();
            while(paramNames.hasMoreElements()){
                String paramName = paramNames.nextElement();
                String paramValue = request.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }

            // 解析body
            String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
            if(StringUtil.isNotEmpty(body)){
                String[] params = StringUtil.splitString(body,"&");
                if(ArrayUtil.isNotEmpty(params)){
                    for(String param : params){
                        String[] array = StringUtil.splitString(param,"=");
                        if(ArrayUtil.isNotEmpty(array)&&array.length == 2){
                            String paramName = array[0];
                            String paramValue = array[1];
                            paramMap.put(paramName,paramValue);
                        }
                    }
                }
            }

            Param param = new Param(paramMap);
            // 调用action方法
            Method actionMethod = handler.getActionMethod();
            Object result = ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);

            // 处理action返回值
            if(request instanceof View){

                // 返回jsp
                View view = (View) result;
                String path = view.getPath();
                if(StringUtil.isNotEmpty(path)){
                    // 解析path
                    if(path.startsWith("/")){
                        response.sendRedirect(request.getContextPath()+path);
                    }else{
                        Map<String,Object> model = view.getModel();
                        for (Map.Entry<String,Object>entry:model.entrySet()){
                            request.setAttribute(entry.getKey(),entry.getValue());
                        }

                        request.getRequestDispatcher(
                                ConfigHelper.getAppJspPath()+path)
                                .forward(request,response);
                    }
                }

            }else if(request instanceof Data){
                // 返回json数据
                Data data = (Data) result;
                Object model = data.getModel();
                if(model != null){
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer = response.getWriter();
                    String json = JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }

            }



        }





    }
}