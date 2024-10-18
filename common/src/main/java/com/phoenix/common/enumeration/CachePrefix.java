package com.phoenix.common.enumeration;

import lombok.Getter;

@Getter
public enum CachePrefix {
    BASE_UPVOTE("BASE_UPVOTE"),
    BASE_ARTICLE_CONTENT("BASE_ARTICLE_CONTENT"),
    USER_CONTENT("USER_CONTENT"),
    USER_SESSION("USER_SESSION");

    private final String name;
    CachePrefix(String name){
        this.name = name;
    }
}
