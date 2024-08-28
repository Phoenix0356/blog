package com.phoenix.filter.listener;

import com.phoenix.filter.filter.matcher.ACAutomaton;
import com.phoenix.filter.listener.event.WordEvent;
import com.phoenix.filter.manager.WordManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WordEventLister {

    private final WordManager wordManager;
    private final ACAutomaton automaton;


    @EventListener
    @Async
    public void onDataUpdated(WordEvent event) {
        //如果事件类型为上传文件
        if (event.getEventType() == WordEvent.EventType.UPLOAD_FILE) {
            automaton.init(wordManager.loadAllWord());
            log.info("监听到文件上传事件，重新从数据库加载数据完成");
        }
    }
}
