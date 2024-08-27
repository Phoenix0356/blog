package com.phoenix.filter.handler;


import com.phoenix.common.constant.CommonConstant;
import com.phoenix.common.exceptions.clientException.BaseException;
import com.phoenix.common.vo.ResultVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResultVO exceptionHandler(BaseException ex){
        if (ex.getMessage().equals(CommonConstant.RE_LOGIN)) return ResultVO.reLogin(ex.getMessage());
        return ResultVO.error(ex.getMessage());
    }
}
