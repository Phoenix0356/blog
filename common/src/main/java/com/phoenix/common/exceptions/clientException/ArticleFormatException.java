package com.phoenix.common.exceptions.clientException;


import com.phoenix.common.constant.RespMessageConstant;

public class ArticleFormatException extends BaseException{
    public ArticleFormatException() {
        super(RespMessageConstant.ARTICLE_FORMAT_ERROR);
    }

    public ArticleFormatException(String msg) {
        super(msg);
    }
}
