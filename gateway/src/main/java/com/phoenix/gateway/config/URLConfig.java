package com.phoenix.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class URLConfig {
    @Value("${service.user.port}")
    public String userServicePort;

    @Value("${server.url.login}")
    public String loginUrl;

    @Value("${server.url.logout}")
    public String logoutUrl;

    @Value("${server.url.register}")
    public String registerUrl;

    @Value("${server.url.session-set}")
    public String sessionSetUrl;

    @Value("${server.url.token-gen}")
    public String tokenGenUrl;

    public static final String HTTP_LOCALHOST = "http://localhost:";
}
