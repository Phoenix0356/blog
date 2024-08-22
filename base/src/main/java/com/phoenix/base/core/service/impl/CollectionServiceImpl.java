package com.phoenix.base.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.common.constant.CommonConstant;
import com.phoenix.common.constant.RespMessageConstant;
import com.phoenix.base.core.mapper.CollectionMapper;
import com.phoenix.base.core.service.ArticleService;
import com.phoenix.base.core.service.CollectionService;
import com.phoenix.common.dto.ArticleNoteDTO;
import com.phoenix.common.dto.CollectionAddDTO;
import com.phoenix.common.dto.CollectionDTO;
import com.phoenix.base.model.entity.Collection;
import com.phoenix.base.util.DataUtil;
import com.phoenix.common.exceptions.clientException.AlreadyContainsException;
import com.phoenix.common.exceptions.clientException.CollectionExistException;
import com.phoenix.common.exceptions.clientException.NotFoundException;
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
    private final ArticleService articleService;

    @Override
    public List<ArticleVO> getAllArticleList(String collectionId) {
        return collectionMapper.selectCollectionArticleList(collectionId);
    }

    @Override
    public CollectionVO getCollection(String collectionId) {
        Collection collection = collectionMapper.selectById(collectionId);
        return CollectionVO.generateCollectionVO(collection);
    }

    @Override
    public List<CollectionVO> getAllCollections(String username) {
        return collectionMapper.selectCollectionsByUsername(username);
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
    public void saveArticleIntoCollection(CollectionAddDTO collectionAddDTO, String userId) {
        Collection collection = collectionMapper.selectOne(new QueryWrapper<Collection>()
                .eq("collection_user_id",userId)
                .eq("collection_name",collectionAddDTO.getCollectionName())
        );

        String collectionId = collection.getCollectionId();
        String articleId = collectionAddDTO.getArticleId();

        if (collectionMapper.isArticleExistsInCollection(collectionId,articleId) == 1){
            throw new AlreadyContainsException(RespMessageConstant.COLLECTION_ALREADY_CONTAINS_ERROR);
        }

        Map<String,String> map = new HashMap<>();
        map.put("collectionId",collectionId );
        map.put("articleId", articleId);
        map.put("collectionArticleListId", UUID.randomUUID().toString());
        collectionMapper.insertArticleIntoCollection(map);
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
        //Todo:线程不安全
        articleService.deleteArticleBookmarkCount(articleId);
        collectionMapper.deleteArticleFromCollection(collectionId,articleId);
    }

    @Override
    public void deleteCollectionById(String collectionId) {
        collectionMapper.deleteById(collectionId);
    }
}
