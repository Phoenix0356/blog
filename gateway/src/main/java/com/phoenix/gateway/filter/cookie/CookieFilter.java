package com.phoenix.gateway.filter.cookie;



import com.phoenix.gateway.config.URLConfig;
import com.phoenix.gateway.enumeration.FilterOrder;
import jakarta.servlet.http.Cookie;
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

import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class CookieFilter implements GlobalFilter, Ordered {

    final WebClient.Builder webClientBuilder;
    final URLConfig urlConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        Optional<String> cookie = Optional.ofNullable(request.getHeaders().getFirst(HttpHeaders.COOKIE));
        if(cookie.isEmpty()) {
            return webClientBuilder.build()
                    .put()
                    .uri(URLConfig.HTTP_LOCALHOST
                            +urlConfig.userServicePort
                            +urlConfig.sessionSetUrl
                    ).retrieve()
                    .bodyToMono(String.class)
                    .flatMap(resp -> {
                        Cookie cookieGenerated = CookieUtil.generateDefaultCookie(CookieFields.SID.name(),resp);
                        String cookieString = CookieUtil.cookieToString(cookieGenerated);
                        response.getHeaders().add(HttpHeaders.SET_COOKIE, cookieString);
                        return chain.filter(exchange);
                    });
        }
        else {
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return FilterOrder.First.getOrder();
    }
}
