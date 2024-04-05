package com.mall4j.cloud.common.exception;

/**
 * @author lwq
 * @date 2022/12/11 5:48 下午 星期日
 * @since 1.0.0
 */
public class RateLimitException extends RuntimeException {
    public RateLimitException(String message) {
        super(message);
    }
}