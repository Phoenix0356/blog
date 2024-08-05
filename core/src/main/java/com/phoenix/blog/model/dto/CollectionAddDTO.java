package com.phoenix.blog.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class CollectionAddDTO {
    String username;
    String articleId;
    String collectionName;
}
