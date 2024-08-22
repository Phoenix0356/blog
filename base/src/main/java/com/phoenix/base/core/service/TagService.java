package com.phoenix.base.core.service;

import com.phoenix.common.dto.ArticleAddTagDTO;
import com.phoenix.common.dto.TagDTO;
import com.phoenix.base.model.vo.TagVO;

import java.util.List;

public interface TagService {
    List<TagVO> getTagList();

    List<TagVO> getArticleTagsList(String articleId);

    void saveTag(TagDTO tagDTO);

    void updateTagToArticle(ArticleAddTagDTO articleAddTagDTO);

    void updateTag(TagDTO tagDTO);

    void  deleteTagById(String tagId);

}
