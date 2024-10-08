package com.phoenix.base.enumeration;

import lombok.Getter;

@Getter
public enum DataStateType {
    NO_OPERATION("NO_OPERATION",0b00),
    UPVOTE("UPVOTE",0b01),
    BOOKMARK("BOOKMARK",0b10),

    UPVOTE_CANCEL("UPVOTE_CANCEL",0b100),
    BOOKMARK_CANCEL("BOOKMARK_CANCEL",0b101);


    private final String name;
    private final int identifier;

    DataStateType(String name, int identifier){
        this.name = name;
        this.identifier = identifier;
    }
}
