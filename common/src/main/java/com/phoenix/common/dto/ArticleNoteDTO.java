package com.phoenix.common.dto;

import com.phoenix.common.annotation.FilterEntity;
import com.phoenix.common.annotation.FilterField;
import lombok.Data;
@FilterEntity
@Data
public class ArticleNoteDTO {
    String articleId;

    @FilterField
    String articleNote;
}
