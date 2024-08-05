package com.phoenix.blog.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.constant.RespMessageConstant;
import com.phoenix.blog.constant.SortConstant;
import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.core.manager.*;
import com.phoenix.blog.core.mapper.ArticleMapper;
import com.phoenix.blog.core.service.ArticleService;
import com.phoenix.blog.core.service.MessageService;
import com.phoenix.blog.enumeration.MessageType;
import com.phoenix.blog.exceptions.clientException.NotFoundException;
import com.phoenix.blog.model.dto.ArticleDTO;
import com.phoenix.blog.model.entity.Article;
import com.phoenix.blog.exceptions.clientException.ArticleFormatException;
import com.phoenix.blog.exceptions.clientException.InvalidateArgumentException;
import com.phoenix.blog.model.entity.ArticleStatic;
import com.phoenix.blog.model.entity.User;
import com.phoenix.blog.model.pojo.LinkedConcurrentMap;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    final ArticleMapper articleMapper;
    final MessageService messageService;

    final ArticleTagManager articleTagManager;
    final ArticleStaticManager articleStaticManager;
    final UserManager userManager;
    final CommentManager commentManager;
    final ArticleManager articleManager;
    static final LinkedConcurrentMap<String,ReentrantLock> articleStaticsLockPool = new LinkedConcurrentMap<>();

    @Override
    public ArticleVO getArticleDetailById(String articleId) {
        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

        Article article;
        ArticleStatic articleStatic;
        User user;

        ReentrantLock reentrantLock = articleStaticsLockPool.getIfAbsent(articleId, ReentrantLock.class);
        reentrantLock.lock();
        try{
            //获取缓存
            article = articleManager.selectArticleInCache(articleId);
            user = userManager.select(article.getArticleUserId());
            //更新阅读量
            articleStatic = articleStaticManager.selectByArticleId(articleId);
            articleStatic.setArticleReadCount(articleStatic.getArticleReadCount()+1);
            articleStaticManager.update(articleStatic);
        }finally {
            reentrantLock.unlock();
        }
        return ArticleVO.buildVO(article,articleStatic,user);
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

        ArticleStatic articleStatic = new ArticleStatic();
        articleStatic.setArticleId(article.getArticleId())
                .setArticleReadCount(0)
                .setArticleUpvoteCount(0)
                .setArticleBookmarkCount(0);
        User user = userManager.select(article.getArticleUserId());
        return ArticleVO.buildVO(article,articleStatic,user);
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

            ArticleStatic articleStatic = articleStaticManager.selectByArticleId(articleId);

            int newUpvoteCount = articleStatic.getArticleUpvoteCount()+articleDTO.getArticleUpvoteCountChange();
            int newBookmarkCount = articleStatic.getArticleBookmarkCount()+articleDTO.getArticleBookmarkCountChange();
            int messageType = articleDTO.getArticleMessageType();

            //判断是否被点赞
            if (DataUtil.isOptionChosen(messageType, MessageType.UPVOTE.getTypeNum())){
                articleStatic.setArticleUpvoteCount(newUpvoteCount);
                messageService.saveMessage(articleId,MessageType.UPVOTE,TokenContext.getUserId());
            }

            //判断是否被收藏
            if (DataUtil.isOptionChosen(messageType, MessageType.BOOKMARK.getTypeNum())){
                articleStatic.setArticleBookmarkCount(newBookmarkCount);
                messageService.saveMessage(articleId,MessageType.BOOKMARK,TokenContext.getUserId());
            }

            //判断是否被取消收藏
            if (DataUtil.isOptionChosen(messageType, MessageType.BOOKMARK_CANCEL.getTypeNum())){
                articleStatic.setArticleBookmarkCount(newBookmarkCount);
                messageService.saveMessage(articleId,MessageType.BOOKMARK_CANCEL,TokenContext.getUserId());
            }

            articleStaticManager.update(articleStatic);
        }finally{
            reentrantLock.unlock();
        }
    }

    @Override
    public void deleteArticleBookmarkCount(String articleId) {
        ReentrantLock reentrantLock = articleStaticsLockPool.getIfAbsent(articleId, ReentrantLock.class);
        reentrantLock.lock();
        try {
            ArticleStatic articleStatic = articleStaticManager.selectByArticleId(articleId);
            articleStatic.setArticleBookmarkCount(articleStatic.getArticleBookmarkCount()-1);
            articleStaticManager.update(articleStatic);
            messageService.saveMessage(articleId, MessageType.BOOKMARK_CANCEL,TokenContext.getUserId());
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
