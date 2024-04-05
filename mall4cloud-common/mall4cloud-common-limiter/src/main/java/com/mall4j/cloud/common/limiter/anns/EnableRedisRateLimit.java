package com.mall4j.cloud.common.limiter.anns;

import com.mall4j.cloud.common.limiter.config.RateRateLimitConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lwq
 * @date 2022/12/11 5:58 下午 星期日
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({RateRateLimitConfiguration.class})
public @interface EnableRedisRateLimit {

}
