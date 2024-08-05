package com.phoenix.blog.core.service.impl;

import com.phoenix.blog.constant.RespMessageConstant;
import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.core.manager.ArticleManager;
import com.phoenix.blog.core.manager.ArticleTagManager;
import com.phoenix.blog.core.mapper.TagMapper;
import com.phoenix.blog.core.service.TagService;
import com.phoenix.blog.exceptions.clientException.NotOwnerException;
import com.phoenix.blog.model.dto.ArticleAddTagDTO;
import com.phoenix.blog.model.dto.TagDTO;
import com.phoenix.blog.model.entity.Article;
import com.phoenix.blog.model.entity.ArticleTagRelation;
import com.phoenix.blog.model.entity.Tag;
import com.phoenix.blog.model.vo.TagVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    final private TagMapper tagMapper;
    final private ArticleManager articleManager;
    final private ArticleTagManager articleTagManager;

    @Override
    public List<TagVO> getTagList() {
        return tagMapper.selectList(null).stream()
                .map(TagVO::buildVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TagVO> getArticleTagsList(String articleId) {
        return tagMapper.getArticleTagList(articleId);
    }

    @Override
    public void saveTag(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setTagContent(tagDTO.getTagContent())
                .setTagReviseTime(new Timestamp(System.currentTimeMillis()))
                .setTagCreateUserId(TokenContext.getUserId());
        tagMapper.insert(tag);
    }

    @Override
    public void updateTagToArticle(ArticleAddTagDTO articleAddTagDTO){

        String articleId = articleAddTagDTO.getArticleId();

        Article article = articleManager.selectArticleInCache(articleId);
        if (!article.getArticleUserId().equals(TokenContext.getUserId())){
            throw new NotOwnerException(RespMessageConstant.ARTICLE_NOT_OWNER_ERROR);
        }
        //删除原有的文章-标签关系

        List<ArticleTagRelation> originRelationList = articleTagManager.selectListByArticleId(articleId);
        articleTagManager.deleteBatch(originRelationList);
        //插入新的文章-标签关系
        List<ArticleTagRelation> inputRelationList = articleAddTagDTO.getTagIdList().stream()
                .map(tagId->new ArticleTagRelation()
                        .setArticleTagListId(UUID.randomUUID().toString())
                        .setArticleId(articleId)
                        .setTagId(tagId))
                .toList();
        inputRelationList.forEach(articleTagManager::insert);
    }

    @Override
    public void updateTag(TagDTO tagDTO) {
        Tag tag = tagMapper.selectById(tagDTO.getTagId());
        tag.setTagReviseTime(new Timestamp(System.currentTimeMillis()))
                .setTagContent(tagDTO.getTagContent());
        tagMapper.updateById(tag);
    }

    @Override
    public void deleteTagById(String tagId) {
        //同步删除关系表中的相关记录
        articleTagManager.deleteBatch(articleTagManager.selectListByTagId(tagId));
        tagMapper.deleteById(tagId);
    }
}
