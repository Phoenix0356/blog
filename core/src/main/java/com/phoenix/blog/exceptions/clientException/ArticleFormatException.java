package com.phoenix.blog.exceptions.clientException;

import com.phoenix.blog.constant.RespMessageConstant;

public class ArticleFormatException extends BaseException{
    public ArticleFormatException() {
        super(RespMessageConstant.ARTICLE_FORMAT_ERROR);
    }

    public ArticleFormatException(String msg) {
        super(msg);
    }
}
