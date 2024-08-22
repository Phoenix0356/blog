//package com.phoenix.gateway.filter;
//
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//
//import java.nio.charset.StandardCharsets;
//
//@Component
//public class SensitiveWordFilter extends AbstractGatewayFilterFactory<SensitiveWordFilter.Config> {
//
//    private final WebClient webClient;
//
//    public SensitiveWordFilter(WebClient.Builder webClientBuilder) {
//        super(Config.class);
//        this.webClient = webClientBuilder.baseUrl("http://filter").build();
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> {
//            if (exchange.getRequest().getMethod() == HttpMethod.POST || exchange.getRequest().getMethod() == HttpMethod.PUT) {
//                return webClient.post()
//                        .uri("/text")
//                        .bodyValue(exchange.getRequest().getBody())
//                        .retrieve()
//                        .bodyToMono(Object.class)
//                        .flatMap((filteredBody)->{
//                            // 将 filteredBody 转换为字节数组
//                            byte[] bytes = filteredBody.toString().getBytes(StandardCharsets.UTF_8);
//
//                            // 创建一个新的 DataBuffer
//                            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
//
//                            // 创建一个新的 ServerHttpRequestDecorator，并将修改后的请求体设置进去
//                            ServerHttpRequestDecorator decoratedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
//                                @Override
//                                public Flux<DataBuffer> getBody() {
//                                    return Flux.just(buffer);
//                                }
//                            };
//
//                            // 使用修改后的 ServerHttpRequest 创建一个新的 ServerWebExchange
//                            ServerWebExchange modifiedExchange = exchange.mutate().request(decoratedRequest).build();
//
//                            return chain.filter(modifiedExchange);
//                        });
//            }
//            return chain.filter(exchange);
//        };
//    }
//
//    public static class Config {
//        // Configuration properties if needed
//    }
//}

