package com.mall4j.cloud.common.cache.constant;

/**
 * @author lhd
 * @date 2021/03/31
 */
public interface SeckillCacheNames {

    /**
     * 前缀
     */
    String SECKILL_PREFIX = "mall4cloud_seckill:";

    /**
     * 秒杀信息
     */
    String SECKILL_BY_SECKILL_ID = SECKILL_PREFIX + "seckill_by_seckill_id:";

    /**
     * 秒杀sku信息
     */
    String SECKILL_SKU_BY_SECKILL_ID = SECKILL_PREFIX + "skulist_by_seckill_id:";

    /**
     * 秒杀sku信息
     */
    String SECKILL_SKU_BY_ID = SECKILL_PREFIX + "sku_by_id:";

    /**
     * 根据商品id获取秒杀信息
     */
    String SECKILL_BY_SPU_ID = "seckill_by_spu_id:";

    String CATEGORY_LIST = SECKILL_PREFIX + "category:list:";
}
