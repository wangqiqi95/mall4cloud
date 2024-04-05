package com.mall4j.cloud.user.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author cl
 * @date 2021-05-11 15:48:02
 */
@Configuration
@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
public class UserThreadConfig {

    /**
     * 因为每台机器不同，所以线程数量应该是通过配置文件进行配置的
     * 主要用来多线程导出用户信息
     * @param pool 线程池配置信息
     * @return 用户信息线程池
     */
    @Bean
    public ThreadPoolExecutor userThreadPoolExecutor(ThreadPoolConfigProperties pool) {
        return new ThreadPoolExecutor(
                pool.getCoreSize(),
                pool.getMaxSize(),
                pool.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                new ThreadFactoryBuilder()
                        .setNameFormat("User-Thread-Pool-%d").build()
        );
    }
}
