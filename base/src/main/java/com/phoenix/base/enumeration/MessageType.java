package com.phoenix.base.enumeration;

import lombok.Getter;

@Getter
public enum MessageType {
    UPVOTE("UPVOTE",0b0001),
    UPVOTE_CANCEL("UPVOTE_CANCEL",0b0010),
    BOOKMARK("BOOKMARK",0b0100),
    BOOKMARK_CANCEL("BOOKMARK_CANCEL",0b1000);

    private final String name;
    private final int identifier;

    MessageType(String name,int identifier){
        this.name = name;
        this.identifier = identifier;
    }
}
