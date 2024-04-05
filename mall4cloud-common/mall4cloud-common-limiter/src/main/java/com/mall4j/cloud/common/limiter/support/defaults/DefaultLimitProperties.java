package com.mall4j.cloud.common.limiter.support.defaults;

import com.mall4j.cloud.common.limiter.support.LimitProperties;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @author lwq
 * @date 2022/12/11 5:53 下午 星期日
 * @since 1.0.0
 */
@NoArgsConstructor
public class DefaultLimitProperties implements LimitProperties {

    private  int replenishRate;

    private  int burstCapacity;

    private  TimeUnit timeUnit;

    public DefaultLimitProperties(int replenishRate, int burstCapacity, TimeUnit timeUnit) {
        this.replenishRate = replenishRate;
        this.burstCapacity = burstCapacity;
        this.timeUnit = timeUnit;
    }

    @Override
    public int replenishRate() {
        return replenishRate;
    }

    @Override
    public int burstCapacity() {
        return burstCapacity;
    }

    @Override
    public TimeUnit timeUnit() {
        return timeUnit;
    }
}
