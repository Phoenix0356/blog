package com.phoenix.filter.util;

public class TextUtil {
    public static void appendReplaceChar(StringBuilder stringBuilder,char replaceChar,int charCount){
        stringBuilder.append(String.valueOf(replaceChar).repeat(Math.max(0, charCount)));
    }
}
