package com.phoenix.gateway.filter;

import com.phoenix.common.enumeration.Authorization;
import com.phoenix.gateway.config.URLConfig;
import com.phoenix.gateway.enumeration.FilterOrder;
import com.phoenix.gateway.filter.cookie.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenFilter implements GlobalFilter, Ordered {
    final WebClient.Builder webClientBuilder;
    final URLConfig urlConfig;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String cookieString = request.getHeaders().getFirst(HttpHeaders.COOKIE);
        if (cookieString == null) {
            cookieString = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        }

        if (cookieString!=null&& !cookieString.isEmpty()) {
            String cookieValue = CookieUtil.getCookieValue(cookieString);
            return webClientBuilder.build()
                    .get()
                    .uri(URLConfig.HTTP_LOCALHOST
                            +urlConfig.userServicePort
                            +urlConfig.tokenGenUrl
                            +"/"
                            +cookieValue
                    ).retrieve()
                    .bodyToMono(String.class)
                    .flatMap(resp->{
                        log.info("生成Token成功: {}", resp);
                        //构建新的请求
                        ServerHttpRequest newRequest = exchange
                                .getRequest()
                                .mutate()
                                .header(HttpHeaders.AUTHORIZATION,
                                        Authorization.PREFIX.getValue()+resp)
                                .build();
                        return chain.filter(exchange.mutate().request(newRequest).build());
                    });
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return FilterOrder.Second.getOrder();
    }
}
