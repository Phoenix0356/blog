package com.phoenix.base.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.common.client.UserServiceClient;
import com.phoenix.common.constant.RespMessageConstant;
import com.phoenix.common.constant.SortConstant;
import com.phoenix.base.context.TokenContext;
import com.phoenix.base.core.manager.*;
import com.phoenix.base.core.mapper.ArticleMapper;
import com.phoenix.base.core.service.ArticleService;
import com.phoenix.base.core.service.MessageService;
import com.phoenix.common.enumeration.MessageType;
import com.phoenix.common.exceptions.clientException.NotFoundException;
import com.phoenix.common.dto.ArticleDTO;
import com.phoenix.base.model.entity.Article;
import com.phoenix.common.exceptions.clientException.ArticleFormatException;
import com.phoenix.common.exceptions.clientException.InvalidateArgumentException;
import com.phoenix.base.model.entity.ArticleData;
import com.phoenix.base.model.pojo.LinkedConcurrentMap;
import com.phoenix.base.util.DataUtil;
import com.phoenix.base.model.vo.ArticleVO;
import com.phoenix.common.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    final UserServiceClient userServiceClient;

    final ArticleMapper articleMapper;
    final MessageService messageService;
    final ArticleTagManager articleTagManager;
    final ArticleStaticManager articleStaticManager;
    final CommentManager commentManager;
    final ArticleManager articleManager;
    static final LinkedConcurrentMap<String,ReentrantLock> articleStaticsLockPool = new LinkedConcurrentMap<>();

    @Override
    public ArticleVO getArticleDetailById(String articleId) {
        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

        Article article;
        ArticleData articleData;
        ResultVO userResultVo;

        ReentrantLock reentrantLock = articleStaticsLockPool.getIfAbsent(articleId, ReentrantLock.class);
        reentrantLock.lock();
        try{
            //获取缓存
            article = articleManager.selectArticleInCache(articleId);

            userResultVo = userServiceClient.getUserById(article.getArticleUserId());
            Object user = userResultVo.getObject();
            userResultVo.getClazz().cast(user);
            //更新阅读量
            articleData = articleStaticManager.selectByArticleId(articleId);
            articleData.setArticleReadCount(articleData.getArticleReadCount()+1);
            articleStaticManager.update(articleData);
        }finally {
            reentrantLock.unlock();
        }
        return ArticleVO.buildVO(article,articleData);
    }

    @Override
    public List<ArticleVO> getArticleAll(int sortStrategy) {
        List<ArticleVO> articleVOList = articleMapper.selectArticlePreviewList();

        switch (sortStrategy){
            case (SortConstant.SORT_BY_READ_COUNT):
                articleVOList.sort(Comparator.comparing(ArticleVO::getArticleReadCount,Comparator.reverseOrder()));
                break;
            case (SortConstant.SORT_BY_UPVOTE_COUNT):
                articleVOList.sort(Comparator.comparing(ArticleVO::getArticleUpvoteCount,Comparator.reverseOrder()));
                break;
            default:
                break;
        }

        return articleVOList;
    }

    @Override
    public List<ArticleVO> getArticleUserList(String userId) {
        return articleMapper.selectUserArticleList(userId);
    }

    @Override
    public ArticleVO saveArticleByUser(ArticleDTO articleDTO) {
        if (DataUtil.isEmptyData(articleDTO.getArticleTitle())){
            throw new ArticleFormatException(RespMessageConstant.ARTICLE_TITLE_EMPTY_ERROR);
        }
        if (DataUtil.isEmptyData(articleDTO.getArticleContent())){
            throw new ArticleFormatException(RespMessageConstant.ARTICLE_CONTENT_EMPTY_ERROR);
        }

        Article article = new Article();
        article.setArticleUserId(articleDTO.getArticleUserId())
        .setArticleTitle(articleDTO.getArticleTitle())
        .setArticleContent(articleDTO.getArticleContent())
        .setArticleReviseTime(new Timestamp(System.currentTimeMillis()));
        articleMapper.insert(article);

        ArticleData articleData = new ArticleData();
        articleData.setArticleId(article.getArticleId())
                .setArticleReadCount(0)
                .setArticleUpvoteCount(0)
                .setArticleBookmarkCount(0);
        articleStaticManager.insert(articleData);

        return ArticleVO.buildVO(article,articleData);
    }

    @Override
    public void updateArticleContent(ArticleDTO articleDTO) {
        String articleId = articleDTO.getArticleId();
        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

        ReentrantLock reentrantLock = articleStaticsLockPool.getIfAbsent(articleId, ReentrantLock.class);
        reentrantLock.lock();
        try {
            articleManager.deleteArticleInCache(articleId);
            Article article = articleMapper.selectById(articleId);
            if (article == null) throw new NotFoundException(RespMessageConstant.ARTICLE_NOT_FOUND_ERROR);

            article.setArticleTitle(articleDTO.getArticleTitle())
                    .setArticleContent(articleDTO.getArticleContent())
                    .setArticleReviseTime(new Timestamp(System.currentTimeMillis()));
            articleMapper.updateById(article);
        }finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public void updateArticleStatics(ArticleDTO articleDTO) {
        ReentrantLock reentrantLock = articleStaticsLockPool.getIfAbsent(articleDTO.getArticleId(), ReentrantLock.class);
        reentrantLock.lock();
        try {
            String articleId = articleDTO.getArticleId();

            if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

            Article article = articleManager.selectArticleInCache(articleId);
            ArticleData articleData = articleStaticManager.selectByArticleId(articleId);

            int newUpvoteCount = articleData.getArticleUpvoteCount()+articleDTO.getArticleUpvoteCountChange();
            int newBookmarkCount = articleData.getArticleBookmarkCount()+articleDTO.getArticleBookmarkCountChange();
            int messageType = articleDTO.getArticleMessageType();

            //判断是否被点赞
            if (DataUtil.isOptionChosen(messageType, MessageType.UPVOTE.getTypeNum())){
                articleData.setArticleUpvoteCount(newUpvoteCount);
                messageService.saveMessage(articleId,MessageType.UPVOTE,
                        TokenContext.getUserId(),article.getArticleUserId());
            }

            //判断是否被收藏
            if (DataUtil.isOptionChosen(messageType, MessageType.BOOKMARK.getTypeNum())){
                articleData.setArticleBookmarkCount(newBookmarkCount);
                messageService.saveMessage(articleId,MessageType.BOOKMARK,
                        TokenContext.getUserId(),article.getArticleUserId());
            }

            //判断是否被取消收藏
            if (DataUtil.isOptionChosen(messageType, MessageType.BOOKMARK_CANCEL.getTypeNum())){
                articleData.setArticleBookmarkCount(newBookmarkCount);
                messageService.saveMessage(articleId,MessageType.BOOKMARK_CANCEL,
                        TokenContext.getUserId(),article.getArticleUserId());
            }

            articleStaticManager.update(articleData);
        }finally{
            reentrantLock.unlock();
        }
    }

    @Override
    public void deleteArticleBookmarkCount(String articleId) {
        ReentrantLock reentrantLock = articleStaticsLockPool.getIfAbsent(articleId, ReentrantLock.class);
        reentrantLock.lock();
        try {
            Article article = articleManager.selectArticleInCache(articleId);
            ArticleData articleStatic = articleStaticManager.selectByArticleId(articleId);
            articleStatic.setArticleBookmarkCount(articleStatic.getArticleBookmarkCount()-1);
            articleStaticManager.update(articleStatic);
            messageService.saveMessage(articleId, MessageType.BOOKMARK_CANCEL,
                    TokenContext.getUserId(),article.getArticleUserId());
        }finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public void deleteArticleById(String articleId) {
        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();
        Article article = articleMapper.selectById(articleId);
        if (article == null) throw new NotFoundException(RespMessageConstant.ARTICLE_NOT_FOUND_ERROR);

        articleTagManager.deleteBatch(articleTagManager.selectListByArticleId(articleId));
        articleMapper.delete(new QueryWrapper<Article>().eq("article_id",articleId));
    }
}
