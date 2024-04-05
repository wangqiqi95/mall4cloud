package com.mall4j.cloud.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author FrozenWatermelon
 * @date 2020/12/15
 */
@ConfigurationProperties(prefix = "mall4cloud.order.thread")
public class ThreadPoolConfigProperties {

    private Integer coreSize;

    private Integer maxSize;

    private Integer keepAliveTime;

    public Integer getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(Integer coreSize) {
        this.coreSize = coreSize;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public Integer getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(Integer keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    @Override
    public String toString() {
        return "ThreadPoolConfigProperties{" +
                "coreSize=" + coreSize +
                ", maxSize=" + maxSize +
                ", keepAliveTime=" + keepAliveTime +
                '}';
    }
}
