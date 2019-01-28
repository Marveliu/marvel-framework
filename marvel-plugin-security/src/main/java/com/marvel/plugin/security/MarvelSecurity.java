package com.marvel.plugin.security;

import java.util.Set;

/**
 * Marvel Security 接口
 * <br/>
 * 可在应用中实现该接口，或者在 marvel.properties 文件中提供以下基于 SQL 的配置项：
 * <ul>
 * <li>marvel.security.jdbc.authc_query：根据用户名获取密码</li>
 * <li>marvel.security.jdbc.roles_query：根据用户名获取角色名集合</li>
 * <li>marvel.security.jdbc.permissions_query：根据角色名获取权限名集合</li>
 * </ul>
 *
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
