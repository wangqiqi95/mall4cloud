package com.mall4j.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

/**
 * @author FrozenWatermelon
 * @date 2020/7/23
 */
@SpringBootApplication(scanBasePackages = { "com.mall4j.cloud" },exclude = UserDetailsServiceAutoConfiguration.class)
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
