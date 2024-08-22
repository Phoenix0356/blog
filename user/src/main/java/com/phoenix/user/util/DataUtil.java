package com.phoenix.user.util;

public class DataUtil{
    public static boolean isEmptyData(String s){
        return s == null || s.isEmpty();
    }
    public static boolean isOptionChosen(int input,int option){
        return (input&option)>0;
    }

}
