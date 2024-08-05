package com.phoenix.blog.exceptions.clientException;

import com.phoenix.blog.constant.RespMessageConstant;

public class CollectionContainsException extends BaseException{
    public CollectionContainsException() {
        super(RespMessageConstant.COLLECTION_ALREADY_CONTAINS_ERROR);
    }

    public CollectionContainsException(String msg) {
        super(msg);
    }
}
