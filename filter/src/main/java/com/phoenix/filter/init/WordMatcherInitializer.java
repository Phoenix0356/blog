package com.phoenix.filter.init;

import com.phoenix.filter.filter.TextFilter;
import com.phoenix.filter.filter.matcher.WordMatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class WordMatcherInitializer implements ApplicationRunner {
    final WordMatcher wordMatcher;
    final TextFilter textFilter;

    @Override
    public void run(ApplicationArguments args) {
        log.info("敏感词匹配器正在初始化");
        wordMatcher.init();

    }


}
