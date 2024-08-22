package com.phoenix.common.exceptions.clientException;

public class BaseException extends RuntimeException{

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

}
