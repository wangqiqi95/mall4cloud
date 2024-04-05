package com.mall4j.cloud.product.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lhd
 * @date 2020/06/23
 */
@ConfigurationProperties(prefix = "mall4cloud.prod.thread")
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
