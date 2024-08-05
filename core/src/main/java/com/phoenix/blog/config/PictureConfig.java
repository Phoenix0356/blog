package com.phoenix.blog.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Data
public class PictureConfig {
    @Value("${picture.defaultAvatarPath}")
    public String defaultAvatarPath;

    @Value("${picture.defaultAvatarURL}")
    public String defaultAvatarURL;
}
