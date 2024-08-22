package com.phoenix.gateway.filter;

//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import io.netty.buffer.ByteBufAllocator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferFactory;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.core.io.buffer.NettyDataBufferFactory;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//
//@Component
//public class ModifyRequestBodyGlobalFilter implements GlobalFilter {
//
//    private final DataBufferFactory dataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        System.out.println("我被调用了");
////        Flux<DataBuffer> body = request.getBody();
////        body.subscribe(buffer -> {
////            byte[] bytes = new byte[buffer.readableByteCount()];
////            buffer.read(bytes);
////            String requestBody = new String(bytes, StandardCharsets.UTF_8);
////            System.out.println("Request Body: " + requestBody);
////        });
//
////        String accessToken = request.getHeaders().getFirst("accessToken");
////        if (!StringUtils.hasLength(accessToken)) {
////            throw new IllegalArgumentException("accessToken");
////        }
//        // 新建一个ServerHttpRequest装饰器,覆盖需要装饰的方法
//        ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(request) {
//            @Override
//            public Flux<DataBuffer> getBody() {
//                Flux<DataBuffer> body = super.getBody();
//
//                InputStreamHolder holder = new InputStreamHolder();
//                body.subscribe(buffer -> {
//                    byte[] bytes = new byte[buffer.readableByteCount()];
//                    buffer.read(bytes);
//                    String requestBody = new String(bytes, StandardCharsets.UTF_8);
//                    holder.inputStream = buffer.asInputStream();
//                });
//                if (null != holder.inputStream) {
//                    try {
//                        // 解析JSON的节点
//                        JsonNode jsonNode = objectMapper.readTree(holder.inputStream);
////                        Assert.isTrue(jsonNode instanceof ObjectNode, "JSON格式异常");
//                        ObjectNode objectNode = (ObjectNode) jsonNode;
//
//                        // JSON节点最外层写入新的属性
////                        objectNode.put("userId", accessToken);
//                        DataBuffer dataBuffer = dataBufferFactory.allocateBuffer();
//                        String json = objectNode.toString();
//                        dataBuffer.write(json.getBytes(StandardCharsets.UTF_8));
//
//                        return Flux.just(dataBuffer);
//                    } catch (Exception e) {
//                        throw new IllegalStateException(e);
//                    }
//                }else {
//                    System.out.println("我被执行了");
//                    return Flux.just();
//                }
//
//            }
//        };
//        System.out.println("方法结束了");
//        //使用修改后的ServerHttpRequestDecorator重新生成一个新的ServerWebExchange
//        return chain.filter(exchange.mutate().request(decorator).build());
////        return chain.filter(exchange);
//    }
//
//    private static class InputStreamHolder {
//        InputStream inputStream;
//    }
//}

