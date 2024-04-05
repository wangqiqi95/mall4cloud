package com.mall4j.cloud.openapi;

import com.mall4j.cloud.openapi.service.erp.impl.StdHandlerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 类描述：对接接口服务 应用启动类
 */
@SpringBootApplication(scanBasePackages = { "com.mall4j.cloud" },exclude = UserDetailsServiceAutoConfiguration.class)
@EnableFeignClients(basePackages = {"com.mall4j.cloud.**.feign"})
public class OpenApiApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(OpenApiApplication.class, args);
		StdHandlerService.setApplicationContext(configurableApplicationContext);
	}
}
