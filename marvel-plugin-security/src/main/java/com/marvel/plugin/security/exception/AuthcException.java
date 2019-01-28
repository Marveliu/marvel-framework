package com.marvel.plugin.security.exception;

/**
 * 非法访问异常
 *
 * @author Marveliu
 * @since 18/04/2018
 **/

public class AuthcException extends Exception {

    public AuthcException() {
        super();
    }

    public AuthcException(String message) {
        super(message);
    }

    public AuthcException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthcException(Throwable cause) {
        super(cause);
    }
}
