package com.phoenix.blog.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Configuration
@Data
public class URLConfig {
    @Value("${URL.base}")
    public String baseURL;

    @Value("${URL.dns}")
    public String dnsName;


}
