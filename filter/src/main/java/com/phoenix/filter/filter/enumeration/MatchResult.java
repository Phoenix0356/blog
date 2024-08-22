package com.phoenix.filter.filter.enumeration;

import lombok.Getter;

@Getter
public enum MatchResult {
    //匹配结束，未匹配到
    NOT_MATCH(0),
    //处理中
    PROCESSING(-1);


    final int identifier;

    MatchResult(int identifier){
        this.identifier = identifier;
    }


}
