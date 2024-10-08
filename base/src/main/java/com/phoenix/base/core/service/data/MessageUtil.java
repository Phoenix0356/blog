package com.phoenix.base.core.service.data;

public class MessageUtil {
    public static boolean isOptionChosen(int input,int option){
        return (input&option)>0;
    }
}
