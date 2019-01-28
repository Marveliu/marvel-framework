package com.marvel.framework;

/**
 * 配置相关的常量
 *
 * @author Marveliu
 * @since 10/04/2018
 **/

public interface ConfigConstant {

    /**
     * 配置文件名称
     */
    String CONFIG_FILE = "marvel.properties";

    /**
     * jdbc相关
     */
    String JDBC_DRIVER = "marvel.jdbc.driver";
    String JDBC_URL = "marvel.jdbc.url";
    String JDBC_USERNAME = "marvel.jdbc.username";
    String JDBC_PASSWORD = "marvel.jdbc.password";

    /**
     * web app相关目录
     */
    String APP_BASE_PACKAGE = "marvel.app.base_package";
    String APP_JSP_PATH = "marvel.app.jsp_path";
    String APP_ASSET_PATH = "marvel.app.asset_path";
    String APP_UPLOAD_LIMIT = "marvel.app.upload_limit";
}