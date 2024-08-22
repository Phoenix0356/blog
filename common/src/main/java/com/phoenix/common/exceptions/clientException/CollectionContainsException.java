package com.phoenix.common.exceptions.clientException;


import com.phoenix.common.constant.RespMessageConstant;

public class CollectionContainsException extends BaseException{
    public CollectionContainsException() {
        super(RespMessageConstant.COLLECTION_ALREADY_CONTAINS_ERROR);
    }

    public CollectionContainsException(String msg) {
        super(msg);
    }
}
