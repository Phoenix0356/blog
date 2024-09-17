package com.phoenix.base.task;

import com.phoenix.base.core.manager.ArticleUpVoteManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
@RequiredArgsConstructor
@Slf4j
public class UpvoteCacheScheduler {

    final ArticleUpVoteManager articleUpVoteManager;

    @Scheduled(cron = "0 0 8-23 * * ?")
    public void importUpvoteDataFromCache(){
        articleUpVoteManager.importCachePersistence();
        log.info("{} 点赞缓存入库完成", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
