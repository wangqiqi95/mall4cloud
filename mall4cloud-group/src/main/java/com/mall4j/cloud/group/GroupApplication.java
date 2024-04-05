package com.mall4j.cloud.group;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author YXF
 * @date 2021/03/20
 */
@SpringBootApplication(scanBasePackages = { "com.mall4j.cloud" },exclude = UserDetailsServiceAutoConfiguration.class)
@EnableFeignClients(basePackages = {"com.mall4j.cloud.**.feign"})
@EnableAsync
public class GroupApplication {
    public static void main(String[] args) {
        SpringApplication.run(GroupApplication.class, args);
    }
}
