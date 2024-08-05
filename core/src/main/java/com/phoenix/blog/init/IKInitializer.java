package com.phoenix.blog.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;

public class IKInitializer implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Dictionary.initial(DefaultConfig.getInstance());
    }
}
