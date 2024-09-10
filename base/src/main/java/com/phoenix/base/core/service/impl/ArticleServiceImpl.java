package com.phoenix.base.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.common.constant.RespMessageConstant;
import com.phoenix.common.constant.SortConstant;
import com.phoenix.base.context.TokenContext;
import com.phoenix.base.core.manager.*;
import com.phoenix.base.core.mapper.ArticleMapper;
import com.phoenix.base.core.service.ArticleService;
import com.phoenix.base.core.service.MessageService;
import com.phoenix.base.enumeration.MessageType;
import com.phoenix.common.exceptions.clientException.NotFoundException;
import com.phoenix.common.dto.ArticleDTO;
import com.phoenix.base.model.entity.Article;
import com.phoenix.common.exceptions.clientException.ArticleFormatException;
import com.phoenix.common.exceptions.clientException.InvalidateArgumentException;
import com.phoenix.base.model.entity.ArticleData;
import com.phoenix.base.model.pojo.LinkedConcurrentMap;
import com.phoenix.common.util.DataUtil;
import com.phoenix.base.model.vo.ArticleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{
    private final ArticleMapper articleMapper;
    private final MessageService messageService;
    private final ArticleTagManager articleTagManager;
    private final ArticleUpVoteManager articleUpVoteManager;
    private final ArticleDataManager articleDataManager;
    private final ArticleManager articleManager;
    static final LinkedConcurrentMap<String,ReentrantLock> articleStaticsLockPool = new LinkedConcurrentMap<>();

    //TODO：这个方法应该异步修改文章阅读量，加快返回速度
    @Override
    public ArticleVO getArticleDetailById(String articleId) {
        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

        Article article;
        ArticleData articleData;

        ReentrantLock reentrantLock = articleStaticsLockPool.getIfAbsent(articleId, ReentrantLock.class);
        reentrantLock.lock();
        try{
            //获取缓存
            article = articleManager.selectArticleInCache(articleId);
            //更新阅读量
            articleData = articleDataManager.selectByArticleId(articleId);
            articleData.setArticleReadCount(articleData.getArticleReadCount()+1);
            articleDataManager.update(articleData);
        }finally {
            reentrantLock.unlock();
        }

        //TODO:可以抽象出一个类，感觉过于繁琐
        ArticleVO articleVO = ArticleVO.buildVO(article,articleData);
        if (TokenContext.getUserId()!=null){
            if (articleUpVoteManager.isArticleUpvoteByUser(articleId,TokenContext.getUserId())){
                articleVO.setArticleDataState(MessageType.UPVOTE.getIdentifier());
            }else {
                articleVO.setArticleDataState(MessageType.UPVOTE_CANCEL.getIdentifier());
            }
        }else {
            articleVO.setArticleDataState(0);
        }

        return articleVO;
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
        articleDataManager.insert(articleData);

        return ArticleVO.buildVO(article,articleData);
    }

    @Override
    public void updateArticleContent(ArticleDTO articleDTO) {
        String articleId = articleDTO.getArticleId();
        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

        ReentrantLock reentrantLock = articleStaticsLockPool.getIfAbsent(articleId, ReentrantLock.class);
        reentrantLock.lock();
        try {
            Article article = articleMapper.selectById(articleId);
            if (article == null) throw new NotFoundException(RespMessageConstant.ARTICLE_NOT_FOUND_ERROR);

            article.setArticleTitle(articleDTO.getArticleTitle())
                    .setArticleContent(articleDTO.getArticleContent())
                    .setArticleReviseTime(new Timestamp(System.currentTimeMillis()));
            articleMapper.updateById(article);
            articleManager.deleteArticleInCache(articleId);

            System.out.println("我被执行了");
        }finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public void updateArticleData(ArticleDTO articleDTO) {
        ReentrantLock reentrantLock = articleStaticsLockPool.getIfAbsent(articleDTO.getArticleId(), ReentrantLock.class);
        reentrantLock.lock();
        try {
            String articleId = articleDTO.getArticleId();
            String operatorUserId = TokenContext.getUserId();

            if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

            Article article = articleManager.selectArticleInCache(articleId);
            ArticleData articleData = articleDataManager.selectByArticleId(articleId);

            String articleUserId = article.getArticleUserId();
            int articleBookMarkCount = articleData.getArticleBookmarkCount();
            int messageType = articleDTO.getArticleMessageType();
            //TODO:重复代码有点多
            //如果被点赞，就更新缓存，并将事件存入消息表
            if (DataUtil.isOptionChosen(messageType, MessageType.UPVOTE.getIdentifier())){
                articleUpVoteManager.setUserIntoSet(articleId,operatorUserId);
                messageService.saveMessage(articleId,MessageType.UPVOTE,
                        operatorUserId,articleUserId);
            }
            //如果被取消点赞，就更新缓存，并将事件存入消息表
            if (DataUtil.isOptionChosen(messageType, MessageType.UPVOTE_CANCEL.getIdentifier())){
                articleUpVoteManager.deleteUserFromSet(articleId,operatorUserId);
                messageService.saveMessage(articleId,MessageType.UPVOTE_CANCEL,
                        operatorUserId,articleUserId);
            }
            //如果被收藏，将事件存入消息表
            if (DataUtil.isOptionChosen(messageType, MessageType.BOOKMARK.getIdentifier())){
                articleData.setArticleBookmarkCount(articleBookMarkCount+1);
                messageService.saveMessage(articleId,MessageType.BOOKMARK,
                        operatorUserId,articleUserId);
            }

            //如果被取消收藏，将事件存入消息表
            if (DataUtil.isOptionChosen(messageType, MessageType.BOOKMARK_CANCEL.getIdentifier())){
                articleData.setArticleBookmarkCount(articleBookMarkCount-1);
                messageService.saveMessage(articleId,MessageType.BOOKMARK_CANCEL,
                        operatorUserId,articleUserId);
            }

            articleDataManager.update(articleData);
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
            ArticleData articleStatic = articleDataManager.selectByArticleId(articleId);
            articleStatic.setArticleBookmarkCount(articleStatic.getArticleBookmarkCount()-1);
            articleDataManager.update(articleStatic);
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

        articleDataManager.deleteByArticleId(articleId);
        articleTagManager.deleteBatch(articleTagManager.selectListByArticleId(articleId));
        articleMapper.delete(new QueryWrapper<Article>().eq("article_id",articleId));
    }
}
