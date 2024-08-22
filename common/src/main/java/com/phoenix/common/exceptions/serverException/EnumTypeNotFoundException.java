package com.phoenix.common.exceptions.serverException;

import com.phoenix.common.exceptions.clientException.BaseException;

public class EnumTypeNotFoundException extends BaseException {
    public EnumTypeNotFoundException() {
        super("The type not found");
    }

    public EnumTypeNotFoundException(String msg) {
        super(msg);
    }
}
