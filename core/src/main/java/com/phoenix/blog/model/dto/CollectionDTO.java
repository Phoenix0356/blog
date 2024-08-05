package com.phoenix.blog.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CollectionDTO {
    String collectionId;
    String collectionName;
    String collectionDescription;
}
