package com.marvel.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 *
 * @author Marveliu
 * @since 10/04/2018
 **/

public final class StringUtil {

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        if (str != null) {
            str.trim();
        }
        return StringUtils.isEmpty(str);
    }


    /**
     * 判断字符串是否非空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

}
