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

/**
 * 提供常量的相关配置
 * @author Marveliu
 * @since 10/04/2018
 **/

public interface ConfigConstant {

    // 配置文件名称
    String CONFIG_FILE = "marvel.properties";

    // jdbc相关
    String JDBC_DRIVER = "marvel.jdbc.driver";
    String JDBC_URL = "marvel.jdbc.url";
    String JDBC_USERNAME = "marvel.jdbc.username";
    String JDBC_PASSWORD = "marvel.jdbc.password";

    // web app相关目录
    String APP_BASE_PACKAGE = "marvel.app.base_package";
    String APP_JSP_PATH = "marvel.app.jsp_path";
    String APP_ASSET_PATH = "marvel.app.asset_path";
    String APP_UPLOAD_LIMIT = "marvel.app.upload_limit";
}