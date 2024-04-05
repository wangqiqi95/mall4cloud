package com.mall4j.cloud.seckill.config;

import com.mall4j.cloud.common.cache.adapter.CacheTtlAdapter;
import com.mall4j.cloud.common.cache.bo.CacheNameWithTtlBO;
import com.mall4j.cloud.common.cache.constant.SeckillCacheNames;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/18
 */
@Component
public class SeckillCacheTtlAdapter implements CacheTtlAdapter {

    @Override
    public List<CacheNameWithTtlBO> listCacheNameWithTtl() {
        List<CacheNameWithTtlBO> cacheNameWithTtls = new ArrayList<>();
        // 秒杀库存信息缓存10秒
        cacheNameWithTtls.add(new CacheNameWithTtlBO(SeckillCacheNames.SECKILL_SKU_BY_SECKILL_ID, 10));
        cacheNameWithTtls.add(new CacheNameWithTtlBO(SeckillCacheNames.SECKILL_SKU_BY_ID, 10));
        cacheNameWithTtls.add(new CacheNameWithTtlBO(SeckillCacheNames.SECKILL_BY_SPU_ID, 10));
        return cacheNameWithTtls;
    }
}
