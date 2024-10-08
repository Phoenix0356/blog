package com.phoenix.base.model.vo;

import com.phoenix.base.model.entity.Collection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectionVO {
    String collectionId;
    String collectionName;
    String collectionReviseTime;
    String collectionDescription;
    public static CollectionVO buildVO(Collection collection){
        return CollectionVO.builder()
                .collectionId(collection.getCollectionId())
                .collectionName(collection.getCollectionName())
                .collectionReviseTime(collection.collectionReviseTime.toString())
                .collectionDescription(collection.getCollectionDescription())
                .build();
    }
}
