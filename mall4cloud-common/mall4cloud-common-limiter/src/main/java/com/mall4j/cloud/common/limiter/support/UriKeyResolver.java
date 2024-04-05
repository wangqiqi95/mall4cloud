package com.mall4j.cloud.common.limiter.support;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lwq
 * @date 2022/12/11 5:51 下午 星期日
 * @since 1.0.0
 * 根据请求的uri进行限流
 */
public class UriKeyResolver implements KeyResolver {

    @Override
    public String resolve(HttpServletRequest request, ProceedingJoinPoint pjp) {
        return request.getRequestURI();
    }
}
