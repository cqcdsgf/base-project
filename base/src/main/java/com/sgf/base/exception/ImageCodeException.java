package com.sgf.base.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by sgf on 2018\1\22 0022.
 */
public class ImageCodeException extends AuthenticationException {

    public ImageCodeException(String msg) {
        super(msg);
    }

    public ImageCodeException(String msg, Throwable t) {
        super(msg, t);
    }
}
