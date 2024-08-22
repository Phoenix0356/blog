package com.phoenix.common.exceptions.clientException;


import com.phoenix.common.constant.RespMessageConstant;

public class CollectionNotFoundException extends BaseException{
    public CollectionNotFoundException() {
        super(RespMessageConstant.COLLECTION_NOT_FOUND_ERROR);
    }

    public CollectionNotFoundException(String msg) {
        super(msg);
    }
}
