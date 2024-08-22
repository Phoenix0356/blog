package com.phoenix.common.exceptions.clientException;

public class JwtValidatingException extends BaseException{
    public JwtValidatingException() {
        super("Token is invalidate");
    }

    public JwtValidatingException(String msg) {
        super(msg);
    }
}
