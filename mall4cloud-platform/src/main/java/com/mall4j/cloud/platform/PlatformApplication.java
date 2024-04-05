package com.mall4j.cloud.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lhd
 * @date 2020/12/22
 */
@SpringBootApplication(scanBasePackages = { "com.mall4j.cloud" },exclude = UserDetailsServiceAutoConfiguration.class)
@EnableFeignClients(basePackages = {"com.mall4j.cloud.**.feign"})
@EnableAsync
public class PlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformApplication.class, args);
	}

}
