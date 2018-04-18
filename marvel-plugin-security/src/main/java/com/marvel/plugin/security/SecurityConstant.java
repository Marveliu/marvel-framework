package com.marvel.plugin.security;
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
 * 常量接口
 * @author Marveliu
 * @since 18/04/2018
 **/

public interface SecurityConstant {
    String REALMS = "marvel.plugin.security.realms";
    String REALMS_JDBC = "jdbc";
    String REALMS_CUSTOM = "custom";

    String MARVEL_SECURITY = "marvel.plugin.security.custom.class";

    String JDBC_AUTHC_QUERY = "marvel.plugin.security.jdbc.authc_query";
    String JDBC_ROLES_QUERY = "marvel.plugin.security.jdbc.roles_query";
    String JDBC_PERMISSIONS_QUERY = "marvel.plugin.security.jdbc.permissions_query";

    String CACHE = "marvel.plugin.security.cache";
}
