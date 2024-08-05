package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.ArticleAddTagDTO;
import com.phoenix.blog.model.dto.TagDTO;
import com.phoenix.blog.model.vo.TagVO;

import java.util.List;

public interface TagService {
    List<TagVO> getTagList();

    List<TagVO> getArticleTagsList(String articleId);

    void saveTag(TagDTO tagDTO);

    void updateTagToArticle(ArticleAddTagDTO articleAddTagDTO);

    void updateTag(TagDTO tagDTO);

    void  deleteTagById(String tagId);

}
