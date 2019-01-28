package com.marvel.framework;

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
 * <p>
 * loadOnStartup = 0 最先启动，核心类
 *
 * @author Marveliu
 * @since 11/04/2018
 **/

@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherServlet.class);

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        // 初始化helperloader,即初始化整个IOC
        HelperLoader.init();
        // 获取ServletContext对象，注册Servlet
        ServletContext servletContext = servletConfig.getServletContext();
        // 注册处理jsp servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        // 注册处理静态资源的servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
        // 配置文件上传
        UploadHelper.init(servletContext);
    }

    /**
     * 重载service，进行请求的转发
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServletHelper.init(request, response);
        try {
            // 获得请求方法和请求路径
            String requestMethod = request.getMethod().toLowerCase();
            String requsetPath = request.getPathInfo();

            if (requsetPath.equals("/favicon.ico")) {
                return;
            }
            // 获得URl对应的handler
            Handler handler = ControllerHelper.getHandler(requestMethod, requsetPath);
            // 获取Controller类及其Bean实例
            if (handler != null) {
                Class<?> controllerClass = handler.getControllerClass();
                Object controllerBean = BeanHelper.getBean(controllerClass);
                // 创建请求参数对象
                Param param;
                // 判断是否为文件上传
                if (UploadHelper.isMutipart(request)) {
                    param = UploadHelper.createParam(request);
                } else {
                    param = RequestHelper.createParam(request);
                }

                Object result = null;
                // 获得URL对应的方法
                Method actionMethod = handler.getActionMethod();
                // 反射进行方法调用
                if (param.isEmpty()) {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
                } else {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
                }
                if (result instanceof View) {
                    // 服务端html渲染
                    handleViewResult((View) result, request, response);
                } else if (result instanceof Data) {
                    // 返回json
                    handleDataResult((Data) result, response);
                }
            } else {
                LOGGER.info("NO ACTION MATCH:method-" + requestMethod + ",path-" + requsetPath);
            }
        } finally {
            ServletHelper.destroy();
        }
    }

    /**
     * 处理视图结果
     *
     * @param view
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = view.getPath();
        if (StringUtil.isNotEmpty(path)) {
            if (path.startsWith("/")) {
                // 重定向
                response.sendRedirect(request.getContextPath() + path);
            } else {
                // jsp进行渲染
                Map<String, Object> model = view.getModel();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
                request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
            }
        }
    }

    /**
     * 处理Data类的视图
     * 返回json数据
     *
     * @param data
     * @param response
     * @throws IOException
     */
    private void handleDataResult(Data data, HttpServletResponse response) throws IOException {
        Object model = data.getModel();
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