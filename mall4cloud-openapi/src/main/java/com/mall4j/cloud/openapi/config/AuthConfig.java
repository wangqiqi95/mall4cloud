package com.mall4j.cloud.openapi.config;

import com.mall4j.cloud.openapi.filter.IphAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.servlet.DispatcherType;

/**
 * 授权配置
 *
 * @author FrozenWatermelon
 * @date 2020/7/11
 */
@Configuration
public class AuthConfig {

	@Autowired
	private IphAuthFilter iphAuthFilter;

	@Bean
	@Lazy
	public FilterRegistrationBean<IphAuthFilter> filterRegistration() {
		FilterRegistrationBean<IphAuthFilter> registration = new FilterRegistrationBean<>();
		// 添加过滤器
		registration.setFilter(iphAuthFilter);
		// 设置过滤路径，/*所有路径
		registration.addUrlPatterns("/iph/product");
		registration.setName("iphAuthFilter");
		// 设置优先级
		registration.setOrder(0);
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		return registration;
	}

}
