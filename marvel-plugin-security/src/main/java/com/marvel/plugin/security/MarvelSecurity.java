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

import java.util.Set;

/**
 * Marvel Security 接口
 * <br/>
 * 可在应用中实现该接口，或者在 marvel.properties 文件中提供以下基于 SQL 的配置项：
 * <ul>
 *     <li>marvel.security.jdbc.authc_query：根据用户名获取密码</li>
 *     <li>marvel.security.jdbc.roles_query：根据用户名获取角色名集合</li>
 *     <li>marvel.security.jdbc.permissions_query：根据角色名获取权限名集合</li>
 * </ul>
 * @author Marveliu
 * @since 18/04/2018
 **/

public interface MarvelSecurity {
    /**
     * 根据用户名获取密码
     *
     * @param username 用户名
     * @return 密码
     */
    String getPassword(String username);

    /**
     * 根据用户名获取角色名集合
     *
     * @param username 用户名
     * @return 角色名集合
     */
    Set<String> getRoleNameSet(String username);

    /**
     * 根据角色名获取权限名集合
     *
     * @param roleName 角色名
     * @return 权限名集合
     */
    Set<String> getPermissionNameSet(String roleName);
}
