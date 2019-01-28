package com.marvel.plugin.security;

import com.marvel.framework.helper.ConfigHelper;
import com.marvel.framework.util.ReflectionUtil;

/**
 * 从配置文件中获取相关属性
 *
 * @author Marveliu
 * @since 18/04/2018
 **/
public class SecurityConfig {
    public static String getRealms() {
        return ConfigHelper.getString(SecurityConstant.REALMS);
    }

    public static MarvelSecurity getMarvelSecurity() {
        String className = ConfigHelper.getString(SecurityConstant.MARVEL_SECURITY);
        return (MarvelSecurity) ReflectionUtil.newInstance(className);
    }

    public static String getJdbcAuthcQuery() {
        return ConfigHelper.getString(SecurityConstant.JDBC_AUTHC_QUERY);
    }

    public static String getJdbcRolesQuery() {
        return ConfigHelper.getString(SecurityConstant.JDBC_ROLES_QUERY);
    }

    public static String getJdbcPermissionsQuery() {
        return ConfigHelper.getString(SecurityConstant.JDBC_PERMISSIONS_QUERY);
    }

    public static boolean isCache() {
        return ConfigHelper.getBoolean(SecurityConstant.CACHE);
    }
}
