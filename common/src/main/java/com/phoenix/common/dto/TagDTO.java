package com.phoenix.common.dto;

import com.phoenix.common.annotation.FilterEntity;
import com.phoenix.common.annotation.FilterField;
import lombok.Data;

@Data
@FilterEntity
public class TagDTO {
    String tagId;
    @FilterField
    String tagContent;
}
