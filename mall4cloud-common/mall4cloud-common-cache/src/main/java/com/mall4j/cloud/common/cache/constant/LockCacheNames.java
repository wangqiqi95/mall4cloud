package com.mall4j.cloud.common.cache.constant;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
public interface LockCacheNames {

    /**
     * 前缀
     */
    String LOCK_PREFIX = "mall4cloud_lock:";

    /**
     * 确认订单信息缓存
     */
    String LOCK_ORDER_PREFIX = LOCK_PREFIX + "order:";

}
