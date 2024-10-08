package com.phoenix.base.core.service.data;


import com.phoenix.base.core.manager.ArticleUpVoteManager;
import com.phoenix.base.core.manager.CollectionManager;
import com.phoenix.base.enumeration.DataStateType;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleDataStateHandler {
    final ArticleUpVoteManager articleUpVoteManager;
    final CollectionManager collectionManager;

    /*
    * 获取数据库中，用户对文章的点赞收藏情况
    * @return 二位二进制数字，对应枚举类com.phoenix.base.enumeration.DataStateType
    * */
    public int getArticleDataState(String userId, String articleId){
        int articleDataState = DataStateType.NO_OPERATION.getIdentifier();
        //如果是游客，则直接返回无操作
        if(userId == null){
            System.out.println("userId is null");
             return articleDataState;
        }
        //如果已点赞
        if(articleUpVoteManager.isArticleUpvoteByUser(articleId,userId)){
            articleDataState |= DataStateType.UPVOTE.getIdentifier();
        }
        //如果已收藏
        if(collectionManager.isArticleCollectedByUser(articleId,userId)){
            articleDataState |= DataStateType.BOOKMARK.getIdentifier();
        }

        return articleDataState;
    }
}
