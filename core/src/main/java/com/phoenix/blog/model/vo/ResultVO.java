package com.phoenix.blog.model.vo;

import com.phoenix.blog.enumeration.ResultType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultVO {
    String msg;
    Object object;
    int result;

    public static ResultVO success(String message, Object object){
        return new ResultVO(message,object,ResultType.SUCCESS.getResultNum());
    }
    public static ResultVO success(String message){
        return new ResultVO(message,null,ResultType.SUCCESS.getResultNum());
    }

    public static ResultVO error(String message, Object object){
        return new ResultVO(message,object,ResultType.ERROR.getResultNum());
    }

    public static ResultVO error(String message){
        return new ResultVO(message,null,ResultType.ERROR.getResultNum());
    }
    public static ResultVO reLogin(String message){
        return new ResultVO(message,null,ResultType.RE_LOGIN.getResultNum());
    }
}
