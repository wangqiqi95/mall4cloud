package com.mall4j.cloud.gateway.filter;

import com.mall4j.cloud.gateway.service.ResolverUriApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class ApiKeyResolver implements KeyResolver {

    @Autowired
    private ResolverUriApiService resolverUriApiService;

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        log.info("resolve--1> from:{} compar:{}",exchange.getRequest().getPath().value(),resolverUriApiService.getResolveuri());
        boolean flag=false;
        for (String uri: Arrays.asList(resolverUriApiService.getResolveuri().split(","))) {
            if(exchange.getRequest().getPath().value().equals(uri)){
                flag=true;
                break;
            }
        }
        log.info("resolve--2> {}",flag);
//        return flag?Mono.just(""):Mono.just(Objects.requireNonNull(exchange.getRequest().getPath().value()));
        return Mono.just(Objects.requireNonNull(exchange.getRequest().getPath().value()));
    }
}
