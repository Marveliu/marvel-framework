package com.marvel.plugin.security.realm;
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

import com.marvel.plugin.security.MarvelSecurity;
import com.marvel.plugin.security.SecurityConstant;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.Md5CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.util.HashSet;
import java.util.Set;

/**
 * 基于 Marvel 的自定义 Realm（需要实现 MarvelSecurity 接口）
 * @author Marveliu
 * @since 18/04/2018
 **/

public class MarvelCustomRealm extends AuthorizingRealm {

    private final MarvelSecurity marvelSecurity;

    public MarvelCustomRealm(MarvelSecurity marvelSecurity) {
        this.marvelSecurity = marvelSecurity;
        super.setName(SecurityConstant.REALMS_CUSTOM);
        // 使用md5加密
        super.setCredentialsMatcher(new Md5CredentialsMatcher());
    }

    /**
     * 用于授权
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        if (token == null) {
            throw new AuthenticationException("parameter token is null");
        }

        // 获得用户名
        String username = ((UsernamePasswordToken) token).getUsername();
        // 获得密码
        String password = marvelSecurity.getPassword(username);

        // 用户名和密码放入AuthenticaitonInfo中，便于后续的验证
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo();
        authenticationInfo.setPrincipals(new SimplePrincipalCollection(username, super.getName()));
        authenticationInfo.setCredentials(password);
        return authenticationInfo;
    }

    /**
     * 用于认证
     * @param principals
     * @return
     */
    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("parameter principals is null");
        }

        // 获得已经认证的用户名
        String username = (String) super.getAvailablePrincipal(principals);
        // 根据MarvelSecurity获得角色名集合
        Set<String> roleNameSet = marvelSecurity.getRoleNameSet(username);
        // 根据MarvelSecurity获得角色对应的权限集合
        Set<String> permissionNameSet = new HashSet<String>();
        if (roleNameSet != null && roleNameSet.size() > 0) {
            for (String roleName : roleNameSet) {
                Set<String> currentPermissionNameSet = marvelSecurity.getPermissionNameSet(roleName);
                permissionNameSet.addAll(currentPermissionNameSet);
            }
        }
        // 将角色集合以及权限名集合放入到authorization中，便于后序的授权
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roleNameSet);
        authorizationInfo.setStringPermissions(permissionNameSet);
        return authorizationInfo;
    }
}
