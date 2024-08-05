package com.phoenix.blog.enumeration;

import lombok.Getter;

@Getter
public enum ResultType {
    ERROR(0),
    SUCCESS(1),
    RE_LOGIN(2);

    private final int resultNum;
    ResultType(int resultNum){
        this.resultNum = resultNum;
    }
}
