package com.phoenix.gateway.enumeration;

import lombok.Getter;

@Getter
public enum FilterOrder {
    First(1),
    Second(2),
    Third(3)
    ;

    private final int order;
    FilterOrder(int orderVal) {
        this.order = orderVal;
    }

}
