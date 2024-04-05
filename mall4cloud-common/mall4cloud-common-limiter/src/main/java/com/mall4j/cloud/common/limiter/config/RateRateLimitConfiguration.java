package com.mall4j.cloud.common.limiter.config;

import com.mall4j.cloud.common.limiter.interceptor.RateLimitInterceptor;
import com.mall4j.cloud.common.limiter.support.KeyResolver;
import com.mall4j.cloud.common.limiter.support.UriKeyResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;

/**
 * @author lwq
 * @date 2022/12/11 5:54 下午 星期日
 * @since 1.0.0
 */
@Slf4j
public class RateRateLimitConfiguration {

    public RateRateLimitConfiguration() {
        log.info("initialize redis rate limit...");
    }

    @Bean(name = "rateLimitRedisScript")
    public RedisScript<List<Long>> rateLimitRedisScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("META-INF/scripts/redis_rate_limiter.lua")));
        redisScript.setResultType(List.class);
        return redisScript;
    }

    @Bean
    public RateLimitInterceptor rateLimitInterceptor() {
        return new RateLimitInterceptor();
    }

    @Bean
    public KeyResolver keyResolver() {
        return new UriKeyResolver();
    }

}
