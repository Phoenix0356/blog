package com.phoenix.base.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.base.context.TokenContext;
import com.phoenix.base.core.manager.ArticleManager;
import com.phoenix.base.core.manager.CollectionManager;
import com.phoenix.base.core.service.MessageService;
import com.phoenix.base.enumeration.DataStateType;
import com.phoenix.base.model.dto.MessageDTO;
import com.phoenix.base.model.entity.Article;
import com.phoenix.base.model.entity.ArticleCollectionRelation;
import com.phoenix.common.constant.CommonConstant;
import com.phoenix.common.constant.RespMessageConstant;
import com.phoenix.base.core.mapper.CollectionMapper;
import com.phoenix.base.core.service.CollectionService;
import com.phoenix.common.dto.ArticleNoteDTO;
import com.phoenix.common.dto.CollectionAddDTO;
import com.phoenix.common.dto.CollectionDTO;
import com.phoenix.base.model.entity.Collection;
import com.phoenix.common.util.DataUtil;
import com.phoenix.common.exceptions.clientException.AlreadyContainsException;
import com.phoenix.common.exceptions.clientException.CollectionExistException;
import com.phoenix.common.exceptions.clientException.NotFoundException;
import com.phoenix.base.model.entity.Article;
import com.phoenix.base.model.vo.ArticleVO;
import com.phoenix.base.model.vo.CollectionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectionMapper collectionMapper;
    private final ArticleManager articleManager;
    private final CollectionManager collectionManager;
    private final MessageService messageService;

    @Override
    public List<ArticleVO> getAllArticleList(String collectionId) {
        return collectionMapper.selectCollectionArticleList(collectionId);
    }

    @Override
    public CollectionVO getCollection(String collectionId) {
        Collection collection = collectionMapper.selectById(collectionId);
        return CollectionVO.buildVO(collection);
    }

    @Override
    public List<CollectionVO> getAllCollections() {
        return collectionMapper
                .selectList(new QueryWrapper<Collection>().eq("collection_user_id",TokenContext.getUserId()))
                .stream().map(CollectionVO::buildVO)
                .toList();
    }

    @Override
    public void saveCollection(CollectionDTO collectionDTO, String userId) {
        String collectionName = collectionDTO.getCollectionName();
        Collection collection = collectionMapper.selectOne(new QueryWrapper<Collection>()
                .eq("collection_user_id",userId)
                .eq("collection_name",collectionName)
        );

        if (collection != null){
            throw new CollectionExistException();
        }

        collection = new Collection();
        collection.setCollectionUserId(userId)
                .setCollectionName(collectionName)
                .setCollectionDescription(collectionDTO.getCollectionDescription())
                .setCollectionReviseTime(new Timestamp(System.currentTimeMillis())
                );
        collectionMapper.insert(collection);
    }

    @Override
    public void saveArticleIntoCollection(CollectionAddDTO collectionAddDTO) {
        String operatorId = TokenContext.getUserId();
        Collection collection = collectionMapper.selectOne(new QueryWrapper<Collection>()
                .eq("collection_user_id",operatorId)
                .eq("collection_name",collectionAddDTO.getCollectionName())
        );

        String collectionId = collection.getCollectionId();
        String articleId = collectionAddDTO.getArticleId();

        if (collectionMapper.isArticleExistsInCollection(collectionId,articleId) == 1){
            throw new AlreadyContainsException(RespMessageConstant.COLLECTION_ALREADY_CONTAINS_ERROR);
        }

        ArticleCollectionRelation articleCollectionRelation = new ArticleCollectionRelation();
        articleCollectionRelation.setArticleId(articleId)
                .setCollectionId(collectionId);
        collectionManager.insertArticleCollectionManager(articleCollectionRelation);
        Article article = articleManager.selectArticleById(articleId);
        //保存消息
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setArticleId(articleId)
                .setOperatorUserId(operatorId)
                .setArticleUserId(article.getArticleUserId());
        messageService.saveMessage(messageDTO, DataStateType.BOOKMARK);

    }

    @Override
    public void saveNoteIntoArticle(String collectionId, ArticleNoteDTO articleNoteDTO) {
        if (DataUtil.isEmptyData(articleNoteDTO.getArticleNote())){
            articleNoteDTO.setArticleNote(CommonConstant.EMPTY_CONTENT);
        }
        collectionMapper.updateArticleNoteIntoCollection(collectionId,
                articleNoteDTO.getArticleId(),
                articleNoteDTO.getArticleNote());
    }

    @Override
    public void updateCollection(CollectionDTO collectionDTO) {
        Collection collection = collectionMapper.selectById(collectionDTO.getCollectionId());
        if (collection == null){
            throw new NotFoundException(RespMessageConstant.COLLECTION_NOT_FOUND_ERROR);
        }
        collection.setCollectionDescription(collectionDTO.getCollectionDescription())
        .setCollectionName(collectionDTO.getCollectionName())
        .setCollectionReviseTime(new Timestamp(System.currentTimeMillis()));
        collectionMapper.updateById(collection);
    }

    @Override
    public void deleteArticleFromCollect(String collectionId, String articleId) {
//        articleService.deleteArticleBookmarkCount(articleId);
        collectionMapper.deleteArticleFromCollection(collectionId,articleId);
        //保存消息
        Article article = articleManager.selectArticleById(articleId);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setArticleId(articleId)
                .setOperatorUserId(TokenContext.getUserId())
                .setArticleUserId(article.getArticleUserId());
        messageService.saveMessage(messageDTO, DataStateType.BOOKMARK_CANCEL);
    }

    @Override
    public void deleteCollectionById(String collectionId) {
        collectionMapper.deleteById(collectionId);
    }
}
