package com.phoenix.blog.model.vo;

import com.phoenix.blog.model.entity.Tag;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagVO {
    String tagId;
    String tagContent;

    public static TagVO buildVO(Tag tag){
        return TagVO.builder()
                .tagId(tag.getTagId())
                .tagContent(tag.getTagContent())
                .build();
    }
}
