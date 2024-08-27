package com.phoenix.filter.excpetion;

import com.phoenix.common.exceptions.clientException.BaseException;

public class FileUploadException extends BaseException {
    public FileUploadException(String msg) {
        super(msg);
    }
}
