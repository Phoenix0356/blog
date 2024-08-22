package com.phoenix.common.exceptions.clientException;


import com.phoenix.common.constant.RespMessageConstant;

public class CollectionExistException extends BaseException {
    public CollectionExistException() {
        super(RespMessageConstant.COLLECTION_ALREADY_EXISTS_ERROR);
    }

    public CollectionExistException(String msg) {
        super(msg);
    }
}
