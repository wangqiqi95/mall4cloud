package com.mall4j.cloud.gateway.dev;

import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author FrozenWatermelon
 * @date 2021/3/4
 */
@Profile("dev")
@Component
public class DevReactiveLoadBalancerClientFilter extends ReactiveLoadBalancerClientFilter {
    public DevReactiveLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties properties, LoadBalancerProperties loadBalancerProperties) {
        super(clientFactory, properties, loadBalancerProperties);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try{
            DevWebFluxContext.set(exchange);
            return super.filter(exchange, chain);
        } finally {
            DevWebFluxContext.clean();

        }

    }
}
