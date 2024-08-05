package com.phoenix.blog.exceptions.clientException;

public class EnumTypeNotFoundException extends BaseException{
    public EnumTypeNotFoundException() {
        super("The type not found");
    }

    public EnumTypeNotFoundException(String msg) {
        super(msg);
    }
}
