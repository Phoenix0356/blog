package com.phoenix.common.enumeration;

import lombok.Getter;

@Getter
public enum Authorization {
    PREFIX("Bearer ");

    private final String value;

    Authorization(String value) {
        this.value = value;
    }
}
