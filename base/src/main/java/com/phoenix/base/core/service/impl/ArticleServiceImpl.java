package com.phoenix.base.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.base.core.service.data.ArticleDataStateHandler;
import com.phoenix.base.core.service.data.chain.DataChangeChainHandler;
import com.phoenix.base.enumeration.DataStateType;
import com.phoenix.common.constant.RespMessageConstant;
import com.phoenix.common.constant.SortConstant;
import com.phoenix.base.context.TokenContext;
import com.phoenix.base.core.manager.*;
import com.phoenix.base.core.mapper.ArticleMapper;
import com.phoenix.base.core.service.ArticleService;
import com.phoenix.base.model.dto.MessageDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{
    private final ArticleMapper articleMapper;

    private final ArticleTagManager articleTagManager;
    private final ArticleUpVoteManager articleUpVoteManager;
    private final ArticleDataManager articleDataManager;
    private final ArticleManager articleManager;

    private final DataChangeChainHandler dataChangeChainHandler;
    private final ArticleDataStateHandler articleDataStateHandler;
    static final LinkedConcurrentMap<String,ReentrantLock> articleStaticsLockPool = new LinkedConcurrentMap<>();

    @Override
    public ArticleVO getArticleDetailById(String articleId) {
        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

        Article article;
        ArticleData articleData;
        int upvoteCount;

        ReentrantLock reentrantLock = articleStaticsLockPool.getIfAbsent(articleId, ReentrantLock.class);
        reentrantLock.lock();
        try{
            //获取文章静态内容缓存
            article = articleManager.selectArticleInCache(articleId);
            //获取文章动态数据，并更新阅读量
            articleData = articleDataManager.selectByArticleId(articleId);
            articleData.setArticleReadCount(articleData.getArticleReadCount()+1);
            articleDataManager.update(articleData);
            //获取数据库和缓存中的点赞数
             upvoteCount = articleUpVoteManager.getArticleUpvoteCount(articleId);
        }finally {
            reentrantLock.unlock();
        }

        ArticleVO articleVO = ArticleVO.buildVO(article,articleData);
        //获取用户的点赞、收藏情况
        System.out.println(TokenContext.getUserId());
        articleVO.setArticleDataState(articleDataStateHandler
                .getArticleDataState(TokenContext.getUserId(), articleId));
        articleVO.setArticleUpvoteCount(upvoteCount);
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
            int articleDataChangedState = articleDTO.getArticleDataChangedState();

            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setArticleId(articleId)
                    .setArticleDataChangedState(articleDataChangedState)
                    .setArticleDataState(articleDataStateHandler.getArticleDataState(operatorUserId, articleId))
                    .setArticleUserId(articleUserId)
                    .setOperatorUserId(operatorUserId)
                    .setArticleData(articleData);
            dataChangeChainHandler.handle(messageDTO);
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
            ArticleData articleData = articleDataManager.selectByArticleId(articleId);
            String operateUserId = TokenContext.getUserId();
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setArticleId(articleId)
                    .setArticleDataChangedState(DataStateType.BOOKMARK_CANCEL.getIdentifier())
                    .setArticleDataState(articleDataStateHandler.getArticleDataState(operateUserId,articleId))
                    .setArticleUserId(article.getArticleUserId())
                    .setOperatorUserId(operateUserId)
                    .setArticleData(articleData);

            dataChangeChainHandler.handle(messageDTO);
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
