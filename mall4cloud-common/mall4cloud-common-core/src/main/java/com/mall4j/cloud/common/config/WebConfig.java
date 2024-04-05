package com.mall4j.cloud.common.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.mall4j.cloud.common.i18n.LuckLocaleChangeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * 格式处理-导出excel配置
 *
 * @author YXF
 * @date 2021/03/09
 */
@Configuration
public class WebConfig  implements WebMvcConfigurer {

	@Autowired
	private LuckLocaleChangeInterceptor mall4cloudLocaleChangeInterceptor;

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		//调用父类的配置
		WebMvcConfigurer.super.configureMessageConverters(converters);
		//创建FastJson的消息转换器
		FastJsonHttpMessageConverter convert = new FastJsonHttpMessageConverter();
		convert.setSupportedMediaTypes(getSupportedMediaTypes());
		converters.add(convert);
	}

	public List<MediaType> getSupportedMediaTypes() {
		//创建fastJson消息转换器
		List<MediaType> supportedMediaTypes = new ArrayList<>();
		supportedMediaTypes.add(MediaType.ALL);
		return supportedMediaTypes;
	}


	@Bean
	public WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurer() {
			//拦截器
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(mall4cloudLocaleChangeInterceptor).addPathPatterns("/**");
			}
		};
	}
	@Bean
	public SessionLocaleResolver localeResolver() {
		return new SessionLocaleResolver();
	}
}
