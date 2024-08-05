package com.phoenix.blog.exceptions;

import com.phoenix.blog.constant.CommonConstant;
import com.phoenix.blog.exceptions.clientException.BaseException;
import com.phoenix.blog.model.vo.ResultVO;
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
