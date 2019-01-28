package com.marvel.plugin.security.exception;

/**
 * 授权异常(权限无效异常)
 *
 * @author Marveliu
 * @since 18/04/2018
 **/

public class AuthzException extends RuntimeException {

    public AuthzException() {
        super();
    }

    public AuthzException(String message) {
        super(message);
    }

    public AuthzException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthzException(Throwable cause) {
        super(cause);
    }
}
