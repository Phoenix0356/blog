package com.phoenix.common.exceptions.clientException;

public class UserAlreadyLoginException extends BaseException{
    public UserAlreadyLoginException(){
        super("Already login, do not login again");
    }
    public UserAlreadyLoginException(String msg){
        super(msg);
    }
}
