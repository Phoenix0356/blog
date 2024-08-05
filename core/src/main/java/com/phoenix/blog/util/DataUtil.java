package com.phoenix.blog.util;

import java.util.*;
import java.util.stream.Collectors;

public class DataUtil{
    public static boolean isEmptyData(String s){
        return s == null || s.isEmpty();
    }
    public static boolean isOptionChosen(int input,int option){
        return (input&option)>0;
    }

}
