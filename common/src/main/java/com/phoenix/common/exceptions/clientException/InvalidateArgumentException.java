package com.phoenix.common.exceptions.clientException;

public class InvalidateArgumentException extends BaseException{
    public InvalidateArgumentException() {
        super("Invalidate value");
    }

    public InvalidateArgumentException(String msg) {
        super(msg);
    }
}
