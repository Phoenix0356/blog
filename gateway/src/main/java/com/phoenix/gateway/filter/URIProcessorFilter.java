package com.phoenix.gateway.filter;

import com.phoenix.gateway.config.URLConfig;
import com.phoenix.gateway.enumeration.FilterOrder;
import com.phoenix.gateway.filter.cookie.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class URIProcessorFilter implements GlobalFilter, Ordered {
    final URLConfig urlConfig;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String uri = exchange.getRequest().getURI().toString();
        String path = exchange.getRequest().getURI().getPath();
        //如果是登录登出、注册等接口，需要加上cookie的value
        if (path.equals(urlConfig.loginUrl)
                ||path.equals(urlConfig.logoutUrl)
                ||path.equals(urlConfig.registerUrl)
        ){
            //从请求头获取cookie
            String cookieString = request.getHeaders().getFirst(HttpHeaders.COOKIE);
            if (cookieString == null) {
                cookieString = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            }

            if (cookieString != null) {
                String cookieValue = CookieUtil.getCookieValue(cookieString);
                //value设置到uri
                ServerHttpRequest newRequest = exchange
                        .getRequest()
                        .mutate()
                        .uri(URI.create(uri+"/"+cookieValue))
                        .build();
                log.info("接收到注册/登录/登出请求");
                return chain.filter(exchange.mutate().request(newRequest).build());
            }

        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return FilterOrder.Third.getOrder();
    }
}

