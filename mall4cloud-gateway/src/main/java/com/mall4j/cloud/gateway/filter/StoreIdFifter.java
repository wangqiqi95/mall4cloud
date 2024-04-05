package com.mall4j.cloud.gateway.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class StoreIdFifter implements GlobalFilter, Ordered {


    private static final Logger LOGGER = LoggerFactory.getLogger(StoreIdFifter.class);

    private static AntPathMatcher antPathMatcher;

    static {
        antPathMatcher = new AntPathMatcher();
    }

    @Override
    public int getOrder() {
        return -100;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String url = uri.getPath();
        String host = uri.getHost();

        // 从请求头中取出token
        String storeId = request.getHeaders().getFirst("storeId");


        // 将现在的request，添加当前身份信息
        ServerHttpRequest.Builder builder = request.mutate();
        if (StringUtils.isNotEmpty(storeId)) {
            // 需要添加请求参数
//            if (request.getMethod() == HttpMethod.GET) {
                // get请求 处理参数
                return addParameterForGetMethod(exchange, chain, uri, storeId, builder);
//            }

//            if (request.getMethod() == HttpMethod.POST) {
//                // post请求 处理参数
//                MediaType contentType = request.getHeaders().getContentType();
//                if (MediaType.APPLICATION_JSON.equals(contentType)
//                        || MediaType.APPLICATION_JSON_UTF8.equals(contentType)) {
//                    // 请求内容为 application json
//                    return addParameterForPostMethod(exchange, chain, storeId);
//                }
//
//                if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType)) {
//                    // 请求内容为 form data
//                    return addParameterForFormData(exchange, chain, storeId, builder);
//                }
//
//            }
//
//            if (request.getMethod() == HttpMethod.PUT) {
//                // put请求 处理参数
//                // 走 post 请求流程
//                return addParameterForPostMethod(exchange, chain, storeId);
//            }
//
//            if (request.getMethod() == HttpMethod.DELETE) {
//                // delete请求 处理参数
//                // 走 get 请求流程
//                return addParameterForGetMethod(exchange, chain, uri, storeId, builder);
//            }

        }


        // 当前过滤器filter执行结束
        return chain.filter(exchange.mutate().request(builder.build()).build());
    }


    private Mono<Void> addParameterForGetMethod(ServerWebExchange exchange, GatewayFilterChain chain, URI uri, String storeId, ServerHttpRequest.Builder builder) {
        StringBuilder query = new StringBuilder();

        String originalQuery = uri.getQuery();
        if (StringUtils.isNotEmpty(originalQuery)) {
            query.append(originalQuery);
            if (originalQuery.charAt(originalQuery.length() - 1) != '&') {
                query.append('&');
            }
        }

        query.append("storeId").append("=").append(storeId);

        try {
            URI newUri = UriComponentsBuilder.fromUri(uri).replaceQuery(query.toString()).build().encode().toUri();
            ServerHttpRequest request = exchange.getRequest().mutate().uri(newUri).build();
            return chain.filter(exchange.mutate().request(request).build());
        } catch (Exception e) {
            LOGGER.error("Invalid URI query: " + query.toString(), e);
            // 当前过滤器filter执行结束
            return chain.filter(exchange.mutate().request(builder.build()).build());
        }
    }

//    private Mono<Void> addParameterForPostMethod(ServerWebExchange exchange, GatewayFilterChain chain, String storeId) {
//        ServerRequest serverRequest = new DefaultServerRequest(exchange);
//        AtomicBoolean flag = new AtomicBoolean(false);
//        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap((o) -> {
//            if (o.startsWith("[")) {
//                // body内容为数组，直接返回
//                return Mono.just(o);
//            }
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                Map map = objectMapper.readValue(o, Map.class);
//
//                map.put("storeId", storeId);
//
//                String json = objectMapper.writeValueAsString(map);
//                LOGGER.info("addParameterForPostMethod -> json = {}", json);
//                return Mono.just(json);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return Mono.just(o);
//            }
//        });
//
//        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
//        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, exchange.getRequest().getHeaders());
//        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
//            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
//                @Override
//                public HttpHeaders getHeaders() {
//                    HttpHeaders httpHeaders = new HttpHeaders();
//                    httpHeaders.putAll(super.getHeaders());
//                    httpHeaders.set("Transfer-Encoding", "chunked");
//                    return httpHeaders;
//                }
//                @Override
//                public Flux<DataBuffer> getBody() {
//                    return outputMessage.getBody();
//                }
//            };
//            return chain.filter(exchange.mutate().request(decorator).build());
//        }));
//    }


    private Mono<Void> addParameterForFormData(ServerWebExchange exchange, GatewayFilterChain chain, String storeId, ServerHttpRequest.Builder builder) {
        builder.header("storeId", String.valueOf(storeId));
        ServerHttpRequest serverHttpRequest = builder.build();
        HttpHeaders headers = serverHttpRequest.getHeaders();

        return chain.filter(exchange.mutate().request(serverHttpRequest).build());
    }
}