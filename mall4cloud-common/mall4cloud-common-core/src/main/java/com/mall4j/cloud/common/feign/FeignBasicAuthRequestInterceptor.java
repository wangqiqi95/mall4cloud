package com.mall4j.cloud.common.feign;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author FrozenWatermelon
 * @date 2020/11/27
 */
@Component
@ConditionalOnClass({RequestInterceptor.class})
public class FeignBasicAuthRequestInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(FeignBasicAuthRequestInterceptor.class);

    @Autowired
    private FeignInsideAuthConfig feignInsideAuthConfig;

    @Override
    public void apply(RequestTemplate template) {
        String url = template.url();
        if (!url.startsWith(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX)) {
            throw new LuckException("内部请求请使用 /feign 开头 ");
        }
        // feign的内部请求，往请求头放入key 和 secret进行校验
        template.header(feignInsideAuthConfig.getKey(), feignInsideAuthConfig.getSecret());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String authorization = request.getHeader("Authorization");


        if (StrUtil.isNotBlank(authorization)) {
            template.header("Authorization", authorization);
        }
        template.header(Constant.LOCALE, LocaleContextHolder.getLocale().toString());
    }
}
