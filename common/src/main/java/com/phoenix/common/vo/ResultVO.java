package com.phoenix.common.vo;

import com.phoenix.common.enumeration.ResultType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultVO {
    String msg;
    Object object;
    Class<?> clazz;
    int result;

    public static ResultVO success(String message, Object object){
        return new ResultVO(message,object,object.getClass(),ResultType.SUCCESS.getResultNum());
    }
    public static ResultVO success(String message){
        return new ResultVO(message,null,null,ResultType.SUCCESS.getResultNum());
    }

    public static ResultVO error(String message, Object object){
        return new ResultVO(message,object,object.getClass(),ResultType.ERROR.getResultNum());
    }

    public static ResultVO error(String message){
        return new ResultVO(message,null,null,ResultType.ERROR.getResultNum());
    }
    public static ResultVO reLogin(String message){
        return new ResultVO(message,null,null,ResultType.RE_LOGIN.getResultNum());
    }
}