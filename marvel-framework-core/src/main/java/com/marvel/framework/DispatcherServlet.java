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
import com.marvel.framework.helper.*;
import com.marvel.framework.util.JsonUtil;
import com.marvel.framework.util.ReflectionUtil;
import com.marvel.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.Map;

/**
 * 请求转发器
 * @author Marveliu
 * @since 11/04/2018
 **/

// 最先启动
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherServlet.class);


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
        UploadHelper.init(servletContext);
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{

        ServletHelper.init(request,response);
        try {
            // 获得请求方法和请求路径
            String requestMethod = request.getMethod().toLowerCase();
            String requsetPath = request.getPathInfo();

            if(requsetPath.equals("/favicon.ico")){
                return;
            }
            // 获得Action处理器
            Handler handler = ControllerHelper.getHandler(requestMethod,requsetPath);
            // 获取Controller类及其Bean实例
            if(handler != null){
                Class<?> controllerClass = handler.getControllerClass();
                Object controllerBean = BeanHelper.getBean(controllerClass);

                // 创建请求参数对象
                Param param;

                if(UploadHelper.isMutipart(request)){
                    param = UploadHelper.createParam(request);
                }else {
                    param = RequestHelper.createParam(request);
                }

                Object result = null;
                // 调用action方法
                Method actionMethod = handler.getActionMethod();

                // action可以无参数
                if(param.isEmpty()){
                    result = ReflectionUtil.invokeMethod(controllerBean,actionMethod);
                }else{
                    result = ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
                }

                // 处理action返回值
                if(result instanceof View){
                    handleViewResult((View) result,request,response);
                }else if(result instanceof Data){
                    handleDataResult((Data) result,response);
                }
            }else {
                LOGGER.info("NO ACTION MATCH:method-"+requestMethod+",path-"+requsetPath);
            }
        }finally {
            ServletHelper.destroy();
        }
    }

    // 处理视图结果
    private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = view.getPath();
        if (StringUtil.isNotEmpty(path)) {
            // 解析path
            if (path.startsWith("/")) {
                response.sendRedirect(request.getContextPath() + path);
            } else {
                Map<String, Object> model = view.getModel();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
                request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
            }
        }
    }

    // 处理Data结果
    private void handleDataResult(Data data, HttpServletResponse response) throws IOException {
        Object model = data.getModel();
        // 返回json数据
        if (model != null) {
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