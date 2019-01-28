package com.marvel.plugin.security.password;

import com.marvel.framework.util.CodecUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * md5密码匹配器
 *
 * @author Marveliu
 * @since 18/04/2018
 **/

public class Md5CredentialsMatcher implements CredentialsMatcher {

    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 从表单过来的明文密码进行md5加密
        String submitted = String.valueOf(((UsernamePasswordToken) token).getPassword());
        // 获取从数据库里已经加密的密码
        String encrypted = String.valueOf(info.getCredentials());
        return CodecUtil.md5(submitted).equals(encrypted);
    }
}
