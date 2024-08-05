package com.phoenix.blog.enumeration;

import lombok.Getter;

@Getter
public enum MessageType {
    UPVOTE("UPVOTE",0b001),
    BOOKMARK("BOOKMARK",0b010),
    BOOKMARK_CANCEL("BOOKMARK_CANCEL",0b100);

    private final String name;
    private final int typeNum;

    MessageType(String name,int typeNum){
        this.name = name;
        this.typeNum = typeNum;
    }
}
