package com.mall4j.cloud.common.cache.constant;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
public interface TimeDiscountCacheNames {

    /**
     *
     * 参考CacheKeyPrefix
     * UserCacheNames 与 key 之间的默认连接字符
     */
    String UNION = "::";

    /**
     * 前缀
     */
    String TIMEDISCOUNT_PREFIX = "mall4cloud_group:";

    String TIMEDISCOUNT_ACTIVITY_SKUS_PREFIX = "mall4cloud_group:activity:skus:";
}
