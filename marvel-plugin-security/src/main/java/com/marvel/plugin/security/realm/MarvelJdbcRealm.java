package com.marvel.plugin.security.realm;

import com.marvel.framework.helper.DatabaseHelper;
import com.marvel.plugin.security.SecurityConfig;
import org.apache.shiro.authc.credential.Md5CredentialsMatcher;
import org.apache.shiro.realm.jdbc.JdbcRealm;

/**
 * 基于 Marvel 的 JDBC Realm（需要提供相关 marvel.plugin.security.jdbc.* 配置项）
 *
 * @author Marveliu
 * @since 18/04/2018
 **/

public class MarvelJdbcRealm extends JdbcRealm {

    public MarvelJdbcRealm() {
        super.setDataSource(DatabaseHelper.getDataSource());
        super.setAuthenticationQuery(SecurityConfig.getJdbcAuthcQuery());
        super.setUserRolesQuery(SecurityConfig.getJdbcRolesQuery());
        super.setPermissionsQuery(SecurityConfig.getJdbcPermissionsQuery());
        super.setPermissionsLookupEnabled(true);
        super.setCredentialsMatcher(new Md5CredentialsMatcher());
    }
}