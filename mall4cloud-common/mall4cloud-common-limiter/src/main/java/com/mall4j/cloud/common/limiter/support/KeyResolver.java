package com.mall4j.cloud.common.limiter.support;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lwq
 * @date 2022/12/11 5:50 下午 星期日
 * @since 1.0.0
 */
public interface KeyResolver {
    /**
     * 具体限流规则
     *
     * @param request request
     * @param pjp
     * @return request
     */
    String resolve(HttpServletRequest request, ProceedingJoinPoint pjp);
}