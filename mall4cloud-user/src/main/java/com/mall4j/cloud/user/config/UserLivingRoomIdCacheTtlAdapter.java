package com.mall4j.cloud.user.config;

import com.mall4j.cloud.common.cache.adapter.CacheTtlAdapter;
import com.mall4j.cloud.common.cache.bo.CacheNameWithTtlBO;
import com.mall4j.cloud.common.cache.constant.UserCacheNames;

import java.util.ArrayList;
import java.util.List;

/**
 * @luzhengxiang
 * @create 2022-03-22 2:43 PM
 **/
public class UserLivingRoomIdCacheTtlAdapter implements CacheTtlAdapter {
    @Override
    public List<CacheNameWithTtlBO> listCacheNameWithTtl() {
        List<CacheNameWithTtlBO> cacheNameWithTtls = new ArrayList<>();
        // 直播间id保存时间24小时
        cacheNameWithTtls.add(new CacheNameWithTtlBO(UserCacheNames.BORROW_LIVING_ROOMG, 60 * 60 *24));
        return cacheNameWithTtls;
    }
}
