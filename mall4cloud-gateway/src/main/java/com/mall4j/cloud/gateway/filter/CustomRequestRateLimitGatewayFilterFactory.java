package com.mall4j.cloud.gateway.filter;

import com.alibaba.nacos.shaded.com.google.common.util.concurrent.RateLimiter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mall4j.cloud.gateway.service.ResolverUriApiService;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 自定义局部限流
 */
@Slf4j
@Component
public class CustomRequestRateLimitGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomRequestRateLimitGatewayFilterFactory.Config> {
    public CustomRequestRateLimitGatewayFilterFactory() {
        super(Config.class);
    }

    @Autowired
    private ResolverUriApiService resolverUriApiService;

    private static final Cache<String, RateLimiter> RATE_LIMITER_CACHE = CacheBuilder
            .newBuilder()
            .maximumSize(1000) //令牌桶总容量
            .expireAfterAccess(1, TimeUnit.HOURS)//每次请求获取的令牌数 / 分钟级别
            .build();

    @Override
    public GatewayFilter apply(Config config) {
        log.info("AbstractGateway-->{}",config.toString());
        return new GatewayFilter() {
            @SneakyThrows
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                String remoteAddr = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
                RateLimiter rateLimiter = RATE_LIMITER_CACHE.get(remoteAddr, () ->
                        RateLimiter.create(Double.parseDouble(config.getPermitsPerSecond())));
                if (rateLimiter.tryAcquire()) {
                    return chain.filter(exchange);
                }

                boolean flag=false;
                for (String uri: Arrays.asList(resolverUriApiService.getResolveuri().split(","))) {
                    if(exchange.getRequest().getPath().value().equals(uri)){
                        flag=true;
                        break;
                    }
                }
                if(!flag){
                    return chain.filter(exchange);
                }
                log.info("AbstractGateway-->{} 限流成功:{}",flag,exchange.getRequest().getPath().value());
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                String back="{\"msg\":\""+resolverUriApiService.getHinit()+"\",\"fail\":true,\"code\":\"A00055\",\"data\":null,\"success\":false}";
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                DataBuffer dataBuffer = response.bufferFactory().wrap(back.getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(dataBuffer));
            }
        };
    }


    /**
     * 用户限流
     * @return
     */
//    @Bean
//    public KeyResolver UserKeyResolver() {
//        return new UserKeyResolver();
//    }

    /**
     * ip限流
     * @return
     */
//    @Bean(value = "ApiKeyResolver")
//    public KeyResolver HostAddrKeyResolver() {
//        return new HostAddrKeyResolver();
//    }

    /**
     * 只能开启其中一个
     * 接口限流
     * @return
     */
//    @Primary
//    @Bean(value = "ApiKeyResolver")
//    public KeyResolver ApiKeyResolver() {
//        return new ApiKeyResolver();
//    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("permitsPerSecond");
    }

    @Data
    public static class Config {
        private String permitsPerSecond="1"; // 令牌桶每秒填充速率
    }
}
