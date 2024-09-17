package com.phoenix.base.enumeration;

import lombok.Getter;

@Getter
public enum CachePrefix {
    UPVOTE("UPVOTE"),
    ARTICLE("ARTICLE");

    private final String name;
    CachePrefix(String name){
        this.name = name;
    }
}
