package com.example.qasystem.basic.exception;

import org.springframework.security.core.AuthenticationException;

public class CaptchaException extends AuthenticationException {

    public CaptchaException(String msg) {
        super(msg);
    }

    @Override
    public synchronized Throwable getCause() {
        return this;
    }
}

