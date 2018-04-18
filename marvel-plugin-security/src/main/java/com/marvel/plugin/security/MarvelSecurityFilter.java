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

import com.marvel.plugin.security.realm.MarvelCustomRealm;
import com.marvel.plugin.security.realm.MarvelJdbcRealm;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.CachingSecurityManager;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.ShiroFilter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Marveliu
 * @since 18/04/2018
 **/

public class MarvelSecurityFilter extends ShiroFilter {

    @Override
    public void init() throws Exception {
        super.init();
        WebSecurityManager webSecurityManager = super.getSecurityManager();
        // 设置realm,支持多个realm，并按照先后的顺序用逗号分割
        setRealms(webSecurityManager);
        // 设置cache减少数据库查询次数
        setCache(webSecurityManager);
    }

    private void setRealms(WebSecurityManager webSecurityManager) {
        // 读取 marvel.plugin.security.realms 配置项
        String securityRealms = SecurityConfig.getRealms();
        if (securityRealms != null) {
            // 多个realm进行“，”分割
            String[] securityRealmArray = securityRealms.split(",");
            if (securityRealmArray.length > 0) {
                // linkedHashSet 单一并且完备
                Set<Realm> realms = new LinkedHashSet<Realm>();
                for (String securityRealm : securityRealmArray) {
                    // 添加基于jdbc的realm,需配置相关的sql查询语句
                    if (securityRealm.equalsIgnoreCase(SecurityConstant.REALMS_JDBC)) {
                        addJdbcRealm(realms);
                    } else if (securityRealm.equalsIgnoreCase(SecurityConstant.REALMS_CUSTOM)) {
                        // 添加订制的realm,需要实现MarvelSecurity接口
                        addCustomRealm(realms);
                    }
                }
                RealmSecurityManager realmSecurityManager = (RealmSecurityManager) webSecurityManager;
                realmSecurityManager.setRealms(realms);
            }
        }
    }

    private void addJdbcRealm(Set<Realm> realms) {
        // 添加自己实现的jdbcRealm
        MarvelJdbcRealm marvelJdbcRealm = new MarvelJdbcRealm();
        realms.add(marvelJdbcRealm);
    }

    private void addCustomRealm(Set<Realm> realms) {
        // 读取配置中marvel.plugin.security.custom.class配置项
        MarvelSecurity marvelSecurity = SecurityConfig.getMarvelSecurity();
        // 添加自己实现的realm
        MarvelCustomRealm marvelCustomRealm = new MarvelCustomRealm(marvelSecurity);
        realms.add(marvelCustomRealm);
    }

    private void setCache(WebSecurityManager webSecurityManager) {
        // 读取marvel.plugin.security.cache配置项
        if (SecurityConfig.isCache()) {
            // 使用基于内存的 CacheManager
            CachingSecurityManager cachingSecurityManager = (CachingSecurityManager) webSecurityManager;
            CacheManager cacheManager = new MemoryConstrainedCacheManager();
            cachingSecurityManager.setCacheManager(cacheManager);
        }
    }
}