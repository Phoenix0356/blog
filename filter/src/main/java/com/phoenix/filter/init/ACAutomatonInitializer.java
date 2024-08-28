package com.phoenix.filter.init;

import com.phoenix.filter.filter.matcher.ACAutomaton;
import com.phoenix.filter.manager.WordManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class ACAutomatonInitializer implements ApplicationRunner {
    final ACAutomaton automaton;
    final WordManager wordManager;

    @Override
    public void run(ApplicationArguments args) {
        automaton.init(wordManager.loadAllWord());
        log.info("敏感词匹配器初始化完成");
        System.out.println(automaton.getSize());
    }
}
