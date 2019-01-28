package com.marvel.plugin.security;


/**
 * 安全相关的访问配置
 *
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
