package com.phoenix.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArticleAddTagDTO {
    String articleId;
    List<String> tagIdList;
}
