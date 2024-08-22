package com.phoenix.common.dto;

import com.phoenix.common.annotation.FilterEntity;
import com.phoenix.common.annotation.FilterField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@FilterEntity
public class CollectionDTO {
    String collectionId;
    @FilterField
    String collectionName;
    @FilterField
    String collectionDescription;
}
