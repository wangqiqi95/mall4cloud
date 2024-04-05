package com.mall4j.cloud.common.cache.constant;

/**
 * @author cl
 * @date 2021-08-14 10:27:01
 */
public interface DistributionCacheNames {

    /**
     *
     * 参考CacheKeyPrefix
     * DistributionCacheNames 与 key 之间的默认连接字符
     */
    String UNION = "::";

    /**
     * 前缀
     */
    String DISTRIBUTION_PREFIX = "mall4cloud_distribution:";

    /**
     * 用户默认地址缓存key
     */
    String DISTRIBUTION_INFO = DISTRIBUTION_PREFIX + "info:";

    /**
     * 根据userId缓存分销员的缓存key
     */
    String DISTRIBUTION_USER_ID = DISTRIBUTION_PREFIX + "distribution_user:user_id:";


    /**
     * 加购商品列表缓存
     */
    String DISTRIBUTION_PURCHASE_RANKING = DISTRIBUTION_PREFIX + "pagepurchaseranking:";
}
