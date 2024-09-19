package com.phoenix.base.core.service.message;

public class MessageUtil {
    public static boolean isOptionChosen(int input,int option){
        return (input&option)>0;
    }
}
