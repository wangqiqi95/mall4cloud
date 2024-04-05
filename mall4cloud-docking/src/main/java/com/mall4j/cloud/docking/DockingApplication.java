package com.mall4j.cloud.docking;

import com.mall4j.cloud.api.docking.skq_sqb.utils.SQBSignUtils;
import com.mall4j.cloud.docking.utils.CrmClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 类描述：对接外部服务接口 应用启动类
 */
@SpringBootApplication(scanBasePackages = { "com.mall4j.cloud" },exclude = UserDetailsServiceAutoConfiguration.class)
@EnableFeignClients(basePackages = {"com.mall4j.cloud.**.feign"})
public class DockingApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(DockingApplication.class, args);
		CrmClients.setContext(applicationContext);
	}
}
