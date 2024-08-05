package com.phoenix.blog.exceptions.clientException;

public class JwtValidatingException extends BaseException{
    public JwtValidatingException() {
        super("Token is invalidate");
    }

    public JwtValidatingException(String msg) {
        super(msg);
    }
}
